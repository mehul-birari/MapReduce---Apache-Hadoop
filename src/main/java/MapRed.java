

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MapRed {
    static Logger log = LoggerFactory.getLogger(Parse.class);
    static Config conf = ConfigFactory.parseResources("application.conf");

    public MapRed() {
    }

    public static void main(String[] argv) throws FileNotFoundException {
        final List<String> csFaculties = new ArrayList();
        File file = new File(conf.getString("csFaculties.name"));
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()) {
            csFaculties.add(sc.nextLine());
        }

        try {
            Properties props = System.getProperties();
            props.setProperty("entityExpansionLimit", "2000000");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                boolean barticle = false;
                boolean bauthor = false;
                List<String> authors = new ArrayList();

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
                        this.barticle = true;
                    }

                    if (qName.equalsIgnoreCase("author") && this.barticle) {
                        this.bauthor = true;
                    }

                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings")) {
                        this.bauthor = false;
                        this.barticle = false;
                        String auth = "";
                        if (this.authors.size() == 1) {
                            log.info("Single author " + (String)this.authors.get(0));
                            Parse.appendStrToFile((String)this.authors.get(0) + "\n");
                        } else {
                            int i;
                            if (this.authors.size() > 1 && this.authors.size() < 3) {
                                for(i = 0; i < this.authors.size(); ++i) {
                                    if (i == 0) {
                                        auth = (String)this.authors.get(i);
                                    } else {
                                        auth = auth + "," + (String)this.authors.get(i);
                                    }
                                }

                                log.info("Collaboration between 2 authors " + auth);
                                Parse.appendStrToFile(auth + "\n");
                            } else if (this.authors.size() > 2) {
                                for(i = 0; i < this.authors.size() - 1; ++i) {
                                    String authPair = "";

                                    for(int j = i + 1; j < this.authors.size(); ++j) {
                                        authPair = (String)this.authors.get(i) + "," + (String)this.authors.get(j);
                                    }

                                    log.info("Collaboration between more than 2 authors " + authPair);
                                    Parse.appendStrToFile(authPair + "\n");
                                }
                            }
                        }

                        this.authors.clear();
                    }

                }

                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (this.bauthor) {
                        String authorName = new String(ch, start, length);
                        if (csFaculties.contains(authorName) && !this.authors.contains(authorName)) {
                            log.info("Add author to list");
                            this.authors.add(authorName);
                        }
                    }

                }
            };
            saxParser.parse("dblp.xml", handler);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public static boolean appendStrToFile(String str) {
        try {
            if (str != null) {
                BufferedWriter out = new BufferedWriter(new FileWriter(conf.getString("outputfileName.output"), true));
                out.write(str);
                log.info(str);
                out.close();
                return true;
            } else {
                return false;
            }
        } catch (IOException var2) {
            log.error("exception occoured" + var2);
            return false;
        }
    }
}
