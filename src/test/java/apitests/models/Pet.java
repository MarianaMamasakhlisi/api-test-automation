package apitests.models;

import java.util.List;

public class Pet {

    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Pet pet = new Pet();

        public Builder id(long id) {
            pet.setId(id);
            return this;
        }

        public Builder name(String name) {
            pet.setName(name);
            return this;
        }

        public Builder category(long id, String name) {
            pet.setCategory(new Category(id, name));
            return this;
        }

        public Builder photoUrls(List<String> photoUrls) {
            pet.setPhotoUrls(photoUrls);
            return this;
        }

        public Builder tags(List<Tag> tags) {
            pet.setTags(tags);
            return this;
        }

        public Builder status(String status) {
            pet.setStatus(status);
            return this;
        }

        public Pet build() {
            return pet;
        }
    }
}
