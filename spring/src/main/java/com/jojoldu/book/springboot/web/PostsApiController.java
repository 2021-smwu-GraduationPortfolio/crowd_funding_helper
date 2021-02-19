package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.service.posts.PostsService;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById (@PathVariable Long id){
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }
    @GetMapping("/supporter")
    public String Supporter(Model model){

        return "<html>\n" +
                "<body bgcolor=#d4a3ae>\n" +
                "\n" +
                "<center>\n" +
                "\n" +
                "    <h1> supporter </h1><br>\n" +
                "\n" +
                "    <form method=\"POST\", action=\"/supporterpage\">\n" +
                "        <b> user name :  <input type=\"text\", name='a', placeholder=\"enter 1\"> <br><br></b>\n" +
                "        <input type=\"submit\" , value='predict!' >\n" +
                "    </form>\n" +
                "\n" +
                "\n" +
                "</center>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
    }
}
