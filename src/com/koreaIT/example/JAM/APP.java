package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.koreaIT.example.JAM.util.DBUtil;
import com.koreaIT.example.JAM.util.SecSql;

public class App {

	public void start() {
		System.out.println("=프로그램 시작==");
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("명령어 ) ");
			String cmd = sc.nextLine().trim();

			Connection conn = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) {
				System.out.println("드라이버 로딩 실패");
				e.printStackTrace();
			}
 
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			try {
				conn = DriverManager.getConnection(url, "root", "");

				int actionRs = doAction(conn, cmd, sc);

				if (actionRs == -1) {
					System.out.println("==프로그램 종료==");
					break;
				}

			} catch (SQLException e) {
				System.out.println("에러 : " + e);
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		sc.close();

	}

	private int doAction(Connection conn, String cmd, Scanner sc) {

		if (cmd.equals("exit")) {
			return -1;
		}
		
		if (cmd.equals("member join")) {
		String loginId=null;
		String loginPw=null;
		String name=null;
		while(true) {
				System.out.printf("아이디 : ");
				loginId = sc.nextLine().trim();
				if(loginId==null||loginId=="") {
					System.out.println("아이디가 입력되지 않았습니다.");
					continue;
				}
			
			
			while(true) {
				System.out.printf("비밀번호 : ");
				loginPw = sc.nextLine().trim();
				
				if(loginPw==null||loginPw=="") {
					System.out.println("비밀번호가 입력되지 않았습니다.");
					continue;
				}
				
				System.out.printf("비밀번호 확인: ");
				String pwcheck = sc.nextLine().trim();
				
				if(pwcheck==null||pwcheck=="") {
					System.out.println("비밀번호 확인이 입력되지 않았습니다.");
					continue;
				}
				
				if(!loginPw.equals(pwcheck)) {
					System.out.println("비밀번호가 일치하지 않습니다.");
					continue;
				}
				break;
			}
			
			while(true) {
				System.out.printf("이름 : ");
				name = sc.nextLine().trim();
				if(name==null||name=="") {
					System.out.println("이름이 입력되지 않았습니다.");
					continue;
				}
				break;
			}
			
			SecSql sql = new SecSql();
			sql.append("INSERT INTO member");
			sql.append("SET regDate = NOW(),");
			sql.append("updateDate = NOW(),");
			sql.append("loginId = ?,", loginId);
			sql.append("loginPw = ?,", loginPw);
			sql.append("`name` = ?", name);
			
			int id = DBUtil.insert(conn, sql);
			
			System.out.printf("%d번 회원이 가입되었습니다\n", id);

			
			}
		}else if (cmd.equals("article write")) {
			System.out.println("==게시물 작성==");
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();

			SecSql sql = new SecSql();
			sql.append("INSERT INTO article");
			sql.append("SET regDate = NOW(),");
			sql.append("updateDate = NOW(),");
			sql.append("title = ?,", title);
			sql.append("`body` = ?", body);
			
			int id = DBUtil.insert(conn, sql);
			
			System.out.printf("%d번 게시글이 생성되었습니다\n", id);
			
		} else if (cmd.equals("article list")) {
			System.out.println("==게시물 목록==");

			ArrayList<Article> articles = new ArrayList<Article>();

			SecSql sql = new SecSql();
			sql.append("SELECT *");
			sql.append("FROM article");
			sql.append("ORDER BY id DESC");
			
			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
			
			for(Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}
			if (articles.size() == 0) {
				System.out.println("게시글이 없습니다");
				return 0;
			}
			System.out.println("번호	/	제목");
			for (Article article : articles) {
				System.out.printf("%d	/	%s\n", article.id, article.title);
			}
		} else if (cmd.startsWith("article modify")) {

			int id = Integer.parseInt(cmd.split(" ")[2]);
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)>0");
			sql.append("FROM article");
			sql.append("where id = ?", id);
			
			int idCount=DBUtil.selectRowIntValue(conn, sql);
			
			if(idCount==0) {
				System.out.printf("%d 게시글이 없습니다\n",id);
				return 0;
			}
			
			System.out.println("==게시물 수정==");

			System.out.printf("새 제목 : ");
			String newTitle = sc.nextLine();
			System.out.printf("새 내용 : ");
			String newBody = sc.nextLine();

			sql = new SecSql();
			sql.append("UPDATE article ");
			sql.append("SET updateDate = NOW(),");
			sql.append("title = ?,", newTitle);
			sql.append("`body` = ?", newBody);
			sql.append("where id = ?", id);
			
			DBUtil.update(conn, sql);
			
			
		}else if (cmd.startsWith("article delete")) {

			int id = Integer.parseInt(cmd.split(" ")[2]);
			
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)>0");
			sql.append("FROM article");
			sql.append("where id = ?", id);
			
			int idCount=DBUtil.selectRowIntValue(conn, sql);
			
			if(idCount==0) {
				System.out.printf("%d 게시글이 없습니다\n",id);
				return 0;
			}

			sql = new SecSql();
			sql.append("DELETE FROM article ");
			sql.append("WHERE id = ?;", id);

			DBUtil.delete(conn, sql);
			
			System.out.println(id + "번 글이 삭제되었습니다");
			
		}else if (cmd.startsWith("article detail")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)>0");
			sql.append("FROM article");
			sql.append("where id = ?", id);
			
			int idCount=DBUtil.selectRowIntValue(conn, sql);
			
			if(idCount==0) {
				System.out.printf("%d 게시글이 없습니다\n",id);
				return 0;
			}
			
			sql = new SecSql();
			sql.append("SELECT *");
			sql.append("FROM article");
			sql.append("WHERE id = ?;", id);
			
			ArrayList<Article> articles = new ArrayList<Article>();
			
			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
			
			for(Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}
			if (articles.size() == 0) {
				System.out.println("게시글이 없습니다");
				return 0;
			}
			
			for(Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}
			if (articles.size() == 0) {
				System.out.println("게시글이 없습니다");
				return 0;
			}
			System.out.printf("== %d 게시물 목록==\n", id);
			System.out.println("번호	/	제목");
			for (Article article : articles) {
				System.out.println("번호 : "+ article.id);
				System.out.println("작성일 : "+ article.regDate);
				System.out.println("수정일  : "+ article.updateDate);
				System.out.println("제목 : "+ article.title);
				System.out.println("내용 : "+ article.body);
				break;
			}
			
		}
		
		return 0;
	}

	

}
