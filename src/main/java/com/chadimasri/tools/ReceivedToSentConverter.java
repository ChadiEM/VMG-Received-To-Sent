package com.chadimasri.tools;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceivedToSentConverter {

    private static final Charset VMG_ENCODING = Charset.forName("UTF-16LE");

    public static void main(String[] args) throws IOException {
        // Parse input
        String inputDirectory = args[0];
        String outputDirectory = args[1];

        // Processing
        Path inputDirectoryPath = Paths.get(inputDirectory);
        Path outputDirectoryPath = Paths.get(outputDirectory);

        for (File file : inputDirectoryPath.toFile().listFiles()) {
            String input = IOUtils.toString(new FileInputStream(file), VMG_ENCODING);

            String output = convert(input);

            Path outputFilePath = outputDirectoryPath.resolve(file.getName());
            Files.write(outputFilePath, output.getBytes(VMG_ENCODING));
        }
    }

    private static String convert(String input) {
        String dt = "";
        String tel = "";
        String betweenBody = "";

        try(Scanner sc = new Scanner(input)) {
            while (sc.hasNext()) {
                String nextLine = sc.next();
                if (nextLine.startsWith("X-NOK-DT:")) {
                    dt = nextLine.split(":")[1];
                }

                if (nextLine.startsWith("TEL:")) {
                    String split1 = nextLine.split(":")[1];
                    if (!split1.isEmpty()) {
                        tel = split1;
                    }
                }
            }
        }

        Pattern p = Pattern.compile(".*BEGIN:VBODY(.*)END:VBODY.*", Pattern.DOTALL);
        Matcher matcher = p.matcher(input);
        if (matcher.matches()) {
            betweenBody = matcher.group(1);
        }

        StringBuilder output = new StringBuilder();

        output.append("BEGIN:VMSG").append("\n")
                .append("VERSION:1.1").append("\n")
                .append("X-IRMC-STATUS:READ").append("\n")
                .append("X-IRMC-BOX:SENT").append("\n")
                .append("X-NOK-DT:").append(dt).append("\n")
                .append("BEGIN:VCARD").append("\n")
                .append("VERSION:2.1").append("\n")
                .append("N:").append("\n")
                .append("TEL:").append("\n")
                .append("END:VCARD").append("\n")
                .append("BEGIN:VENV").append("\n")
                .append("BEGIN:VCARD").append("\n")
                .append("VERSION:2.1").append("\n")
                .append("N:").append(tel).append("\n")
                .append("TEL:").append(tel).append("\n")
                .append("END:VCARD").append("\n")
                .append("BEGIN:VENV").append("\n")
                .append("BEGIN:VBODY")
                .append(betweenBody)
                .append("END:VBODY").append("\n")
                .append("END:VENV").append("\n")
                .append("END:VENV").append("\n")
                .append("END:VMSG").append("\n");

        return output.toString();

    }
}
