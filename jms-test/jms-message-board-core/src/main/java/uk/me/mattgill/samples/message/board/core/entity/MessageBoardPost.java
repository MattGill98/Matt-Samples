package uk.me.mattgill.samples.message.board.core.entity;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MessageBoardPost implements Serializable {

    @NotNull(message = "Must not be null")
    @Pattern(regexp = "(\\w+\\s+)(\\w+\\s*){1,2}", message = "Must be 2 or 3 words") // Must be 2 or 3 words
    private String author;

    @NotNull(message = "Must not be null")
    @Pattern(regexp = "(\\w+\\s+)(\\w+\\s*){1,}", message = "Must be multiple words") // Must be multiple words
    private String title;

    @NotNull(message = "Must not be null")
    @Size(min = 1, message = "Must not be null")
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
