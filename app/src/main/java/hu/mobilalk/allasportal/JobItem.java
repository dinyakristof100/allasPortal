package hu.mobilalk.allasportal;

import java.util.HashMap;
import java.util.Map;

public class JobItem {

    private String id;
    private String title;
    private String description;
    private String longDescription;
    private float companyRating;
    private String location;
    private int imageResource;

    public JobItem() {
    }

    public JobItem(String id, String title, String description, String longDescription, String location, int imageResource, float companyRating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.longDescription = longDescription;
        this.location = location;
        this.companyRating = companyRating;
        this.imageResource = imageResource;
    }

    public String _getId() { return id; }

    public void setId(String id){ this.id = id;}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLongDescription(){ return longDescription; }

    public float getCompanyRating() {
        return companyRating;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getLocation() {
        return location;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("location", location);
        result.put("imageResource", imageResource);
        result.put("rating", companyRating);
        return result;
    }
}
