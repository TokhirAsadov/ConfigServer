package uz.pdp.configservermain.component;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
@EnableScheduling
public class Schedule {

    boolean bool=true;

    @Scheduled(fixedDelay = 5000,initialDelay = 5000)
    public void changeVariables(){
        if (this.bool) {
            try {
                System.out.println("Scheduling...");
                FileInputStream fileInputStream = new FileInputStream("allClientVariables.properties");
                Scanner scanner = new Scanner(fileInputStream);
                List<String> properties = new ArrayList<>();
                while (scanner.hasNextLine()) {
                    properties.add(scanner.nextLine());
                }
                List<String> changedProperties = new ArrayList<>();
                for (int i = 0; i < properties.size(); i++) {
                    String property = properties.get(i);
                    if (property.substring(0,property.indexOf("=")).toLowerCase().contains("secretkey")){
                        property = property.substring(0, property.indexOf("=") + 1) + UUID.randomUUID();
                        System.out.println(property);
                    }
                    changedProperties.add(i, property);
                }
                System.out.println(changedProperties);
                scanner.close();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("allClientVariables.properties"));
                StringBuilder str = new StringBuilder();
                for (String s : changedProperties) {
                    str.append(s).append("\n");
                }
                bufferedWriter.write(str.toString());
                bufferedWriter.close();
                Runtime.getRuntime().exec("pushingToGit.bat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
