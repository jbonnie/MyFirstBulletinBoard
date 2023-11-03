package myblog.firstproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import myblog.firstproject.entity.Article;

@AllArgsConstructor
@ToString
@Getter
public class ArticleForm {
    private Long id;
    private String title;
    private String content;

    public Article toEntity() {
        return new Article(id, title, content);
    }
}
