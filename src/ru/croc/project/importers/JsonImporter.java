//Попытка создания класса-импортёра из json
// package ru.croc.project.importers;
//
//import javax.json.Json;
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.JsonReader;
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.List;
//
//public class JsonImporter {
//    public JsonImporter(){
//
//    }
//
//    public void importFromJson(){
//        try {
//            File inputFile = new File("films.json");
//
//            JsonReader reader = Json.createReader(new FileReader(inputFile));
//            JsonObject jsonObject = reader.readObject();
//
//            JsonArray jsonArray = jsonObject.getJsonArray("films");
//
//            List<Film> films = new ArrayList<>();
//
//            for (int i = 0; i < jsonArray.size(); i++) {
//                JsonObject filmObject = jsonArray.getJsonObject(i);
//
//                String name = filmObject.getString("name");
//                List<String> genreList = filmObject.getJsonArray("genre").getValuesAs(JsonString -> JsonString.getString());
//
//                Film film = new Film(name, genreList);
//                films.add(film);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
