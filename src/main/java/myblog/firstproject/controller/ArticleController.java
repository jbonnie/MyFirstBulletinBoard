package myblog.firstproject.controller;

import lombok.extern.slf4j.Slf4j;
import myblog.firstproject.dto.ArticleForm;
import myblog.firstproject.entity.Article;
import myblog.firstproject.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @GetMapping("/articles/new")        // 게시물 작성 뷰 템플릿 띄우기
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm a) {        // 게시물 새로 생성하여 DB에 저장
        log.info(a.toString());
        // dto -> entity
        Article article = a.toEntity();
        log.info(article.toString());
        // save entity to DB
        Article saved = articleRepository.save(article);
        System.out.println(saved.getId());
        log.info(saved.toString());
        return "redirect:/articles/" + saved.getId();       // 게시물을 생성하고 나면 생성한 게시물 상세 보기 페이지로 리다이렉트
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id, Model model) {     // id에 해당하는 게시물 띄우기
        Article articleEntity = articleRepository.findById(id).orElse(null);        // id에 해당하는 게시물 엔티티 가져오기
        model.addAttribute("article", articleEntity);       // 모델에 데이터 등록
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {     // 데이터 목록
        ArrayList<Article> articleEntityList = articleRepository.findAll();
        model.addAttribute("articleList", articleEntityList);       // 모델에 게시물 목록 등록
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // 수정할 게시물 엔티티 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", articleEntity);       // 모델에 데이터 등록
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        // dto -> entity 변환
        Article article = form.toEntity();
        // 기존 데이터 가져오기
        Article target = articleRepository.findById(form.getId()).orElse(null);
        // entity를 DB에 저장
        if(target != null) {
            articleRepository.save(article);
        }
        // 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        Article target = articleRepository.findById(id).orElse(null);
        if(target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("message", "Deletion complete.");      // 리다이렉트 시점에 한 번만 사용할 데이터
        }
        return "redirect:/articles";
    }
}
