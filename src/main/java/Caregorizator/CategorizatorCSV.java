package Caregorizator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CategorizatorCSV implements Categorizable {

    private Map<String, String> categoriesMap;
    private static final String OTHER = "другое";

    public CategorizatorCSV(File file) throws IOException {
        categoriesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] categoryPair = line.split("\t");
                categoriesMap.put(categoryPair[0], categoryPair[1]);
            }
        }
    }

    @Override
    public String getCategory(String name) {
        String category;
        if (categoriesMap.containsKey(name.toLowerCase())) {
            category = categoriesMap.get(name.toLowerCase());
        } else {
            category = OTHER;
        }
        return category;
    }

    @Override
    public String[] getListOfCategories() {
        Set<String> allCategoriesSet = new HashSet<>(categoriesMap.values());
        allCategoriesSet.add(OTHER);
        return allCategoriesSet.toArray(new String[0]);
    }
}
