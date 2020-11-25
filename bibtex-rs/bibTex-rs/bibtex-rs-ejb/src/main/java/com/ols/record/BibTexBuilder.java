package com.ols.record;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

@Deprecated
public class BibTexBuilder {
    private String author;
    private String title;
    private String year;
    private String address;
    private String language;
    private String publisher;
    private Set<String> parameters;

    public BibTexBuilder(Map<String, String> patterns) {
        parameters = patterns.keySet();
        for (Map.Entry<String, String> entry : patterns.entrySet()) {
            //firstChar to UpperCase for invoking set-methods
            String nameOfMethod = "set" + entry.getKey().toUpperCase().charAt(0) + entry.getKey().substring(1);
            try {
                this.getClass().getDeclaredMethod(nameOfMethod, new Class[]{String.class}).invoke(this, entry.getValue());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAuthor(String author) {
        this.author = author;
    }

    public String getMainAuthor() {
        return author.split(" ")[0];
    }

    public String getYear() {
        return year;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setYear(String year) {
        this.year = year;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    private void setLanguage(String language) {
        this.language = language;
    }

    private void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String build() throws NoSuchFieldException, IllegalAccessException {
        StringBuffer bibTexText = new StringBuffer();
        bibTexText.append("@book{")
                    .append(getMainAuthor()).append(getYear())
                    .append(',')
                    .append("\n");
        for (String parameter : parameters) {
            String field =  BibTexBuilder.class.getDeclaredField(parameter)
                                                .get(this)
                                                .toString()
                                                .trim();
            if (parameter.equals("author")) //deleting "and" in the end of authors-parameter
                field = field.substring(0, field.length() - 4);
            if (field != null) bibTexText.append("  ")
                                            .append(parameter)
                                            .append("={")
                                            .append(field)
                                            .append("},")
                                            .append("\n");
        }
        //deleting "," from last body-line and adding a closing "}"
        return bibTexText.substring(0, bibTexText.length() - 2) + "\n" + '}';
    }

}
