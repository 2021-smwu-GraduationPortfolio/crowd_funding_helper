package com.jojoldu.book.springboot.web;


import com.jojoldu.book.springboot.security.domain.UserEntity;
import com.jojoldu.book.springboot.security.presentation.MainController;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.json.simple.JSONObject;
import sun.applet.Main;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.Principal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Controller

public class HelloController {

    Connection conn;
    private String email;
    private JdbcTemplate jdbc;

    @Autowired
    MainController mainController = new MainController();

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }


    @GetMapping("/")
    public String Main(Model model, Authentication authentication, Principal principal) throws SQLException {



        if(principal == null){
            System.out.println("로그인 안 함");
            return "mainpage";
        }
        else{
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Connection conn;
            conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3307/test?useSSL=false&serverTimezone=UTC", "root", "Pami1227!*");
            java.sql.Statement stmt = conn.createStatement();
            System.out.println("db 연결 성공");

            StringBuilder sb = new StringBuilder();

            String sql = sb.append("select count(title) from test.picklist where email = '")
                    .append(userDetails.getUsername())
                    .append("';").toString();
            System.out.println("찜 프로젝트 관련 sql : "+sql);
            try{
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()){
                    if(rs.getString("count(title)").equals("0")) {
                        System.out.println("찜한 프로젝트 없음");
                        return "redirect:/categoryResult";
                    }
                }
                System.out.println("찜한 프로젝트 있음");
                return "mainpage";



            }catch (SQLException e){
                System.out.println("찜목록 확인 실패");
                e.printStackTrace();
            }
        }
        return "mainpage";
    }
    @GetMapping("/supporter")
    public String Supporter(Model model){
        return "supporter";
    }

    @GetMapping("/creator")
    public String Creator(Model model){
        return "creator";
    }



    @GetMapping(value = "/supporterpage")
    public String SupporterPage(Principal principal, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        //String username = request.getParameter("a");
        //String username = userEmail;

        System.out.println("origin username : " + username);
        //username = new String(username.getBytes("8859_1"),"utf-8");
        String res = "failed";
        // 소켓을 선언한다.
        List<List<String>> sendList = null;
        JSONArray sendjson = null;
        try (Socket client = new Socket()) {
// 소켓에 접속하기 위한 접속 정보를 선언한다.
            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9000);
// 소켓 접속!
            client.connect(ipep);
// 소켓이 접속이 완료되면 inputstream과 outputstream을 받는다.
            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
// 메시지는 for 문을 통해 10번 메시지를 전송한다.

// 전송할 메시지를 작성한다.
                String msg = "user" + username;
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
// Byte Buffer를 통해 little 엔디언 형식으로 데이터 길이를 구한다.
                ByteBuffer bb = ByteBuffer.wrap(data);
                //System.out.println(String.format("바이트 버퍼 초기 값 : %s", bb));
                    /*while(bb.hasRemaining()){
                        System.out.println(bb.get()+",");
                    }*/
                bb.order(ByteOrder.LITTLE_ENDIAN);
                int length = bb.getInt();
// 데이터를 받을 버퍼를 선언한다.
                data = new byte[length];
// 데이터를 받는다.
                receiver.read(data, 0, length);
                System.out.println(data);
                // byte형식의 데이터를 string형식으로 변환한다.
                res = new String(data, "UTF-8");

                //받은 데이터를 2차원 리스트로 변환해 보낸다.
                int strNum = res.length();

                int variableNum = 0;
                String completeData = "";
                sendjson = new JSONArray();
                JSONObject subjson = new JSONObject();
                //List<fundingList> sendList = new ArrayList<>();
                for (int i = 0; i < strNum; i++) {
                    if (res.charAt(i) == '[') {
                        variableNum = 0;
                        completeData = "";
                        subjson = new JSONObject();
                        continue;
                    } else if (res.charAt(i) == ']') {
                        subjson.put("id", completeData);
                        sendjson.add(subjson);
                        if (res.charAt(i+1) == ']') break;
                        continue;
                    } else if (res.charAt(i) == '\'') {
                        continue;
                    } else if (res.charAt(i) == ',' && res.charAt(i + 1) == ' ') {
                        i += 1;
                        if (variableNum == 0) {
                            subjson.put("category", completeData);
                        }
                        else if (variableNum == 1) subjson.put("title", completeData);
                        else if(variableNum == 2) subjson.put("url", completeData);
                        variableNum += 1;
                        completeData = "";
                        continue;
                    }
                    completeData = completeData + res.charAt(i);
                }
// 콘솔에 출력한다.
                System.out.println(res);
                System.out.println(sendjson);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        model.addAttribute("test1", sendjson);
        return "supporterpage";

    }


    @PostMapping(value = "/creatortitle")
    public String CreatorTitle(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        int t_cnt = 0;
        int w_cnt = 0;
        request.setCharacterEncoding("utf-8");

        String category = request.getParameter("ca");
        String title = request.getParameter("a");

        String res = "failed";
        List<List<String>> sendList = null;
        JSONArray sendjson = null;
        try(Socket client = new Socket()){
            // 소켓에 접속하기 위한 접속 정보를 선언한다.
            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9000);
// 소켓 접속!
            client.connect(ipep);
// 소켓이 접속이 완료되면 inputstream과 outputstream을 받는다.
            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
// 메시지는 for 문을 통해 10번 메시지를 전송한다.

// 전송할 메시지를 작성한다.
                String msg = category+"title" + title;
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
// Byte Buffer를 통해 little 엔디언 형식으로 데이터 길이를 구한다.
                ByteBuffer bb = ByteBuffer.wrap(data);
                //System.out.println(String.format("바이트 버퍼 초기 값 : %s", bb));
                    /*while(bb.hasRemaining()){
                        System.out.println(bb.get()+",");
                    }*/
                bb.order(ByteOrder.LITTLE_ENDIAN);
                int length = bb.getInt();
// 데이터를 받을 버퍼를 선언한다.
                data = new byte[length];
// 데이터를 받는다.
                receiver.read(data, 0, length);
                System.out.println(data);
                // byte형식의 데이터를 string형식으로 변환한다.
                res = new String(data, "UTF-8");

                //받은 데이터를 2차원 리스트로 변환해 보낸다.
                int strNum = res.length();

                int variableNum = 0;
                String completeData = "";
                sendjson = new JSONArray();
                JSONObject subjson = new JSONObject();
                //List<fundingList> sendList = new ArrayList<>();
                for (int i = 0; i < strNum; i++) {
                    if (res.charAt(i) == '[') {
                        variableNum = 0;
                        completeData = "";
                        subjson = new JSONObject();
                        continue;
                    } else if (res.charAt(i) == ']') {
                        subjson.put("id", completeData);
                        sendjson.add(subjson);
                        if (res.charAt(i+1) == ']') break;
                        continue;
                    } else if (res.charAt(i) == '\'') {
                        continue;
                    } else if (res.charAt(i) == ',' && res.charAt(i + 1) == ' ') {
                        i += 1;
                        if (variableNum == 0) {
                            subjson.put("pagename", completeData);
                            if (completeData.equals("tumblbug")){
                                t_cnt = t_cnt + 1;

                            } else if (completeData.equals("wadiz")) {
                                w_cnt = w_cnt + 1;
                            }
                        }
                        else if (variableNum == 1) subjson.put("content", completeData);
                        else if (variableNum == 2) subjson.put("url", completeData);
                        else if (variableNum == 3) subjson.put("achieve", completeData);
                        else if(variableNum == 4) subjson.put("goal", completeData);
                        variableNum += 1;
                        completeData = "";
                        continue;
                    }
                    completeData = completeData + res.charAt(i);
                }
// 콘솔에 출력한다.
                System.out.println(res);
                System.out.println(sendjson);

            }

        }catch (Throwable e) {
            e.printStackTrace();
        }

        model.addAttribute("test1", sendjson);
        model.addAttribute("t_cnt", t_cnt);
        model.addAttribute("w_cnt", w_cnt);
        return "creatorpage";

    }

    @PostMapping(value = "/creatorkeyword")
    public String CreatorKeyword(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");

        int w_cnt = 0;
        int t_cnt = 0;
        String category = request.getParameter("ca");
        String keyword = request.getParameter("a");

        String res = "failed";
        List<List<String>> sendList = null;
        JSONArray sendjson = null;
        try(Socket client = new Socket()){
            // 소켓에 접속하기 위한 접속 정보를 선언한다.
            InetSocketAddress ipep = new InetSocketAddress("127.0.0.1", 9000);
// 소켓 접속!
            client.connect(ipep);
// 소켓이 접속이 완료되면 inputstream과 outputstream을 받는다.
            try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream();) {
// 메시지는 for 문을 통해 10번 메시지를 전송한다.

// 전송할 메시지를 작성한다.
                String msg = category+"keyword" + keyword;
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
// Byte Buffer를 통해 little 엔디언 형식으로 데이터 길이를 구한다.
                ByteBuffer bb = ByteBuffer.wrap(data);
                //System.out.println(String.format("바이트 버퍼 초기 값 : %s", bb));
                    /*while(bb.hasRemaining()){
                        System.out.println(bb.get()+",");
                    }*/
                bb.order(ByteOrder.LITTLE_ENDIAN);
                int length = bb.getInt();
// 데이터를 받을 버퍼를 선언한다.
                data = new byte[length];
// 데이터를 받는다.
                receiver.read(data, 0, length);
                System.out.println(data);
                // byte형식의 데이터를 string형식으로 변환한다.
                res = new String(data, "UTF-8");

                //받은 데이터를 2차원 리스트로 변환해 보낸다.
                int strNum = res.length();

                int variableNum = 0;
                String completeData = "";
                sendjson = new JSONArray();
                JSONObject subjson = new JSONObject();
                //List<fundingList> sendList = new ArrayList<>();
                for (int i = 0; i < strNum; i++) {
                    if (res.charAt(i) == '[') {
                        variableNum = 0;
                        completeData = "";
                        subjson = new JSONObject();
                        continue;
                    } else if (res.charAt(i) == ']') {
                        subjson.put("id", completeData);
                        sendjson.add(subjson);
                        if (res.charAt(i+1) == ']') break;
                        continue;
                    } else if (res.charAt(i) == '\'') {
                        continue;
                    } else if (res.charAt(i) == ',' && res.charAt(i + 1) == ' ') {
                        i += 1;
                        if (variableNum == 0) {
                            subjson.put("pagename", completeData);
                            if (completeData.equals("tumblbug")){
                                t_cnt = t_cnt + 1;

                            } else if (completeData.equals("wadiz")) {
                                w_cnt = w_cnt + 1;
                            }
                        }
                        else if (variableNum == 1) subjson.put("content", completeData);
                        else if (variableNum == 2) subjson.put("url", completeData);
                        else if (variableNum == 3) subjson.put("achieve", completeData);
                        else if(variableNum == 4) subjson.put("goal", completeData);
                        variableNum += 1;
                        completeData = "";
                        continue;
                    }
                    completeData = completeData + res.charAt(i);
                }
// 콘솔에 출력한다.
                System.out.println(res);
                System.out.println(sendjson);

            }

        }catch (Throwable e) {
            e.printStackTrace();
        }

        model.addAttribute("test1", sendjson);
        model.addAttribute("t_cnt", t_cnt);
        model.addAttribute("w_cnt", w_cnt);
        return "creatorpage";

    }

    @RequestMapping(value = "/picklistModeSelect")
    public String picklistModeSelect(Model model){

        return "picklistModeSelect";
    }

    @PostMapping(value = "/picklistResult")
    public String picklistResult(@RequestBody String mode, Model model) throws SQLException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        this.email = user.getUsername();

        conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3307/test?useSSL=false&serverTimezone=UTC", "root", "Pami1227!*");
        java.sql.Statement stmt = conn.createStatement();
        System.out.println("db 연결 성공");

        // System.out.println("mode : " + mode);   // supporter=%ED%9B%84%EC%9B%90%EC%9E%90

        String role = mode.substring(0, mode.indexOf('='));  // =이후 잘라내고 mode 저장
        System.out.println("mode : " + role);

        StringBuilder sb = new StringBuilder();
        String sql;
        if (role.equals("supporter") || role.equals("creator")) {
            sql = sb.append("select distinct title, pagename, url from ")
                    .append("test.picklist ")
                    .append("where email = '")
                    .append(email)
                    .append("' ")
                    .append("and role = '")
                    .append(role)
                    .append("';").toString();
        } else {
            System.out.println("전체보기 선택");
            sql = sb.append("select distinct title, pagename, url from ")
                    .append("test.picklist ")
                    .append("where email = '")
                    .append(email)
                    .append("';").toString();
        }

        System.out.println(sql);

        JSONArray sendtohtml = new JSONArray();

        try {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                JSONObject subtohtml = new JSONObject();
                //System.out.print(rs.getString("title"));
                subtohtml.put("title", rs.getString("title"));
                //System.out.print(rs.getString("pagename"));
                subtohtml.put("pagename", rs.getString("pagename"));
                //System.out.print(rs.getString("url"));
                subtohtml.put("url", rs.getString("url"));
                //System.out.println("subtohtml : " + subtohtml);
                sendtohtml.add(subtohtml);
                //System.out.println("sendtohtml : " + sendtohtml);

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("html에 보낼 json 넣는데 오류");
            e.printStackTrace();
        }

        model.addAttribute("role", role);
        model.addAttribute("picklist", sendtohtml);
        System.out.println("email : " + email + ", role : " + role + " 일때 picklist : " + sendtohtml);
        return "picklistResult";
    }

}