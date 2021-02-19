package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.HelloResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;



@RequiredArgsConstructor
@Controller

public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount){
        return new HelloResponseDto(name, amount);
    }

    @GetMapping("/")
    public String Main(Model model){

        return "mainpage";
    }


    @PostMapping(value = "/supporterpage")
    public String SupporterPage(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        String username = request.getParameter("a");
        username = new String(username.getBytes("8859_1"),"utf-8");
        String res = "failed";
        // 소켓을 선언한다.
        try (Socket client = new Socket()) {
// 소켓에 접속하기 위한 접속 정보를 선언한다.
            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9000);
// 소켓 접속!
            client.connect(ipep);
// 소켓이 접속이 완료되면 inputstream과 outputstream을 받는다.
            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
// 메시지는 for 문을 통해 10번 메시지를 전송한다.

// 전송할 메시지를 작성한다.
                    String msg = "user"+username;
// string을 byte배열 형식으로 변환한다.
                    System.out.println(msg);
                    byte[] data = msg.getBytes();
// ByteBuffer를 통해 데이터 길이를 byte형식으로 변환한다.
                    ByteBuffer b = ByteBuffer.allocate(4);
// byte포멧은 little 엔디언이다.
                    b.order(ByteOrder.LITTLE_ENDIAN);
                    b.putInt(data.length);
// 데이터 길이 전송
                    sender.write(b.array(), 0, 4);
// 데이터 전송
                    sender.write(data);
                    data = new byte[4];
                    System.out.println("data 보내짐");
// 데이터 길이를 받는다.
                    receiver.read(data, 0, 4);
// ByteBuffer를 통해 little 엔디언 형식으로 데이터 길이를 구한다.
                    ByteBuffer bb = ByteBuffer.wrap(data);
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    int length = bb.getInt();
// 데이터를 받을 버퍼를 선언한다.
                    data = new byte[length];
// 데이터를 받는다.
                    receiver.read(data, 0, length);
                    System.out.println(data);
                    // byte형식의 데이터를 string형식으로 변환한다.
                    msg = new String(data, "UTF-8");
                    res = msg;
// 콘솔에 출력한다.
                    System.out.println(res);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }


        model.addAttribute("test1",res);
        return "supporterpage";
    }


}
