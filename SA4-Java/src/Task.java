public class Task {
    // atributos
    private String description;
    private boolean done;

    // construtor (somente String description)
    public Task(String description) {

        this.description = description;
        this.done = false;
    }

    // get and set
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
