package be.smals.convertto.service;

import be.smals.convertto.model.CodeExample;
import be.smals.convertto.model.ConvertTo;
import be.smals.convertto.model.ConvertToSkeleton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by soldiertt on 18-07-14.
 */
@Service("fsService")
public class FileSystemService {

    public static final String APP_BASE_PATH = "F:\\Google Drive\\Private\\prog\\web\\convertto";

    public static final String LINE_BREAK = "\n";

    public static final String CONVERSIONS = "{{conversions}}";

    public static final String STATIC_PAGE_TEMPLATE = "<?php session_start(); ?>" + LINE_BREAK
        + "<?php" + LINE_BREAK
        + "include(\"../../conf.php\");" + LINE_BREAK
        + "include(\"../../session.php\");" + LINE_BREAK
        + "?>" + LINE_BREAK
        + "<!DOCTYPE html>" + LINE_BREAK
        + "<html>" + LINE_BREAK
        + "<?php" + LINE_BREAK
        + "$pageTitle = \"{{lang}} : [{{from}}] To [{{to}}]\";" + LINE_BREAK
        + "$pageDescription = \"Convert '{{from}}' datatype to '{{to}}' in {{lang}}.\";" + LINE_BREAK
        + "include(\"../../parts/head.php\"); ?>" + LINE_BREAK
        + "<body>" + LINE_BREAK
        + "<div id=\"all-container\">" + LINE_BREAK
        + "<?php" + LINE_BREAK
        + "include(\"../../parts/head-nav.php\"); ?>" + LINE_BREAK
        + "<div id=\"content\">" + LINE_BREAK
        + "<form action=\"../../index.php\" method=\"POST\" id=\"convRedirect\">" + LINE_BREAK
        + "<input type=\"hidden\" name=\"fromId\" id=\"fromId\" value=\"{{fromId}}\" />" + LINE_BREAK
        + "<input type=\"hidden\" name=\"toId\" id=\"toId\" value=\"{{toId}}\" />" + LINE_BREAK
        + "</form>" + LINE_BREAK
        + "<div id=\"result\"></div>" + LINE_BREAK
        + "<div id=\"disqus_thread\"></div>" + LINE_BREAK
        + "</div>" + LINE_BREAK
        + "</div>" + LINE_BREAK
        + "<?php" + LINE_BREAK
        + "include(\"../../parts/foot.php\");" + LINE_BREAK
        + "include(\"../../parts/scripts.php\"); ?>" + LINE_BREAK
        + "</body>" + LINE_BREAK
        + "</html>";

    public static final String STATIC_SITEMAP_TEMPLATE = "<?php session_start(); ?>" + LINE_BREAK
            + "<?php" + LINE_BREAK
            + "include(\"conf.php\");" + LINE_BREAK
            + "include(\"session.php\");" + LINE_BREAK
            + "?>" + LINE_BREAK
            + "<!DOCTYPE html>" + LINE_BREAK
            + "<html>" + LINE_BREAK
            + "<?php" + LINE_BREAK
            + "include(\"parts/head.php\"); ?>" + LINE_BREAK
            + "<body>" + LINE_BREAK
            + "<div id=\"sitemap\">" + LINE_BREAK
            + "<header>ConvertTo - Sitemap</header>" + LINE_BREAK
            + "<nav><a href='index.php'>Back to ConvertTo</a></nav>" + LINE_BREAK
            + "{{sitemapcontent}}" + LINE_BREAK
            + "<div id=\"content\">" + LINE_BREAK
            + "</div>" + LINE_BREAK
            + "<?php" + LINE_BREAK
            + "include(\"parts/scripts.php\"); ?>" + LINE_BREAK
            + "</body>" + LINE_BREAK
            + "</html>";

    private ConvertToSkeleton currentConvert;
    private CodeExample currentExample = new CodeExample();

    public List<ConvertToSkeleton> readStreamOfLine() throws IOException {
        List<ConvertToSkeleton> convertToSkeletons = new ArrayList<>();
        Stream<String> lines = Files.lines(Paths.get(APP_BASE_PATH, "data.txt"));
        lines.forEachOrdered(line -> process(line, convertToSkeletons));
        return convertToSkeletons;
    }

    private void process(String line, List<ConvertToSkeleton> convertToSkeletons) {
        if (line.trim().length() > 0) { // Skip blank lines
            String[] tokens = line.split("##");
            if (tokens.length >= 4) { //New convertTo object
                currentConvert = new ConvertToSkeleton();
                currentConvert.setLanguageShortLabel(tokens[1]);
                currentConvert.setDataTypeFrom(tokens[2]);
                currentConvert.setDataTypeTo(tokens[3]);
            } else if (tokens.length == 1) { // Line of code
                currentExample.addLine(line);
            } else if (line.trim().equals("##")) { // Example separator
                // Flush current example
                currentConvert.addCodeExample(currentExample);
                currentExample = new CodeExample();
            } else if (line.trim().equals("##END##")) { //End of convertTo element
                // Flush current example
                currentConvert.addCodeExample(currentExample);
                // Add currentConvert
                convertToSkeletons.add(currentConvert);
                currentExample = new CodeExample();
            }
        }
    }

    public void generateJsonFiles(List<ConvertTo> convertToList) {
        for (ConvertTo convertTo : convertToList) {
            JSONObject obj = new JSONObject();
            JSONArray examples = new JSONArray();
            for (CodeExample codeExample : convertTo.getExampleList()) {
                JSONObject example = new JSONObject();
                JSONArray lines = new JSONArray();
                for (String line : codeExample.getCode()) {
                    lines.add(line);
                }
                example.put("lines", lines);
                examples.add(example);
            }
            obj.put("examples", examples);

            try {

                String baseFolder = APP_BASE_PATH + "\\" + convertTo.getFromType().getLang().getShortLabel();
                File theDir = new File(baseFolder);

                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    System.out.println("creating directory: " + baseFolder);
                    boolean result = false;
                    try{
                        theDir.mkdir();
                        result = true;
                    } catch(SecurityException se){
                        //handle it
                    }
                    if(result) {
                        System.out.println("DIR created");
                    }
                }

                String fileName = convertTo.getFromType().getId() + "_" + convertTo.getToType().getId() + ".json";
                FileWriter file = new FileWriter(baseFolder + "\\" + fileName);
                file.write(obj.toJSONString());
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateStaticFiles(List<ConvertTo> convertToList) {
        for (ConvertTo convertTo : convertToList) {
            String htmlContent = STATIC_PAGE_TEMPLATE;
            htmlContent = htmlContent.replace("{{from}}", convertTo.getFromType().getLabel());
            htmlContent = htmlContent.replace("{{to}}", convertTo.getToType().getLabel());
            htmlContent = htmlContent.replace("{{lang}}", convertTo.getFromType().getLang().getLabel());
            htmlContent = htmlContent.replace("{{fromId}}", convertTo.getFromType().getId().toString());
            htmlContent = htmlContent.replace("{{toId}}", convertTo.getToType().getId().toString());

            try {

                String baseFolder = APP_BASE_PATH + File.separator + convertTo.getFromType().getLang().getShortLabel();
                File theDir = new File(baseFolder);

                // if the directory does not exist, create it
                if (!theDir.exists()) {
                    System.out.println("creating directory: " + baseFolder);
                    boolean result = false;
                    try{
                        theDir.mkdir();
                        result = true;
                    } catch(SecurityException se){
                        //handle it
                    }
                    if(result) {
                        System.out.println("DIR created");
                    }
                }

                String fileName = normalizeString(convertTo.getFromType().getLabel())
                    + "_to_"
                    + normalizeString(convertTo.getToType().getLabel())
                    + "_"
                    + convertTo.getFromType().getId().toString()
                    + "_"
                    + convertTo.getToType().getId().toString()
                    + ".php";
                FileWriter file = new FileWriter(baseFolder + "\\" + fileName);
                file.write(htmlContent);
                file.flush();
                file.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateSitemap(List<ConvertTo> convertToList) {
        String htmlContent = STATIC_SITEMAP_TEMPLATE;
        StringBuilder htmlSitemap = new StringBuilder("");
        String activeLang = "";
        StringBuilder liList = new StringBuilder("");
        for (ConvertTo convertTo : convertToList) {
            // Language changed
            if (!activeLang.equals(convertTo.getFromType().getLang().getShortLabel())) {
                // Previous li's need to be inserted
                int listPos = htmlSitemap.indexOf(CONVERSIONS);
                if (listPos != -1) {
                    htmlSitemap = htmlSitemap.replace(listPos, listPos + CONVERSIONS.length(), liList.toString());
                    liList = new StringBuilder("");
                }
                activeLang = convertTo.getFromType().getLang().getShortLabel();
                htmlSitemap.append("<div class='panel'><h1>").append(convertTo.getFromType().getLang().getLabel())
                        .append("</h1><ul>\n").append(CONVERSIONS).append("</ul></div>\n");
            }
            //Process link
            String fileLink = "lang/" + convertTo.getFromType().getLang().getShortLabel() + "/"
                    + normalizeString(convertTo.getFromType().getLabel())
                    + "_to_"
                    + normalizeString(convertTo.getToType().getLabel())
                    + "_"
                    + convertTo.getFromType().getId().toString()
                    + "_"
                    + convertTo.getToType().getId().toString()
                    + ".php";
            liList.append("<li><a href='").append(fileLink).append("'>")
                    .append(convertTo.getFromType().getLabel()).append(" to ")
                    .append(convertTo.getToType().getLabel())
                    .append("</a></li>\n");

        }
        // Previous li's need to be inserted
        int listPos = htmlSitemap.indexOf(CONVERSIONS);
        if (listPos != -1) {
            htmlSitemap = htmlSitemap.replace(listPos, listPos + CONVERSIONS.length(), liList.toString());
        }

        htmlContent = htmlContent.replace("{{sitemapcontent}}", htmlSitemap.toString());

        String baseFolder = APP_BASE_PATH;

        try {
            FileWriter file = new FileWriter(baseFolder + File.separator + "sitemap.php");
            file.write(htmlContent);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String normalizeString(String datatypeName) {
        return datatypeName.toLowerCase().replaceAll(" ", "_");
    }
}
