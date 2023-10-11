package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class APP {
	public void start() {
	
	System.out.println("=프로그램 시작==");
	Scanner sc = new Scanner(System.in);

	while (true) {
		System.out.print("명령어 ) ");
		String cmd = sc.nextLine().trim();
		
		if (cmd.equals("exit")) {
			break;
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

//		여기서 controller로 이어야겠네.	
			
			
		if (cmd.equals("article write")) {
			System.out.println("==게시물 작성==");

			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();

			Article article = new Article(title, body);


				String sql = "INSERT INTO article";
				sql += " SET regDate = NOW(),";
				sql += "updateDate = NOW(),";
				sql += "title = '" + title + "',";
				sql += "`body` = '" + body + "';";

				pstmt = conn.prepareStatement(sql);

				int affectedRow = pstmt.executeUpdate();


		} else if (cmd.equals("article list")) {
			System.out.println("==게시물 목록==");


			// 결과를 담을 ArrayList생성
			ArrayList<Article> articles = new ArrayList<Article>();


				String sql = "SELECT *";
				sql += " FROM article";
				sql += " ORDER BY id DESC;";


				pstmt = conn.prepareStatement(sql);

				// 결과를 담을 ResultSet 생성 후 결과 담기
				rs = pstmt.executeQuery(sql);

				// ResultSet에 담긴 결과를 ArrayList에 담기
				while (rs.next()) {
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);

					articles.add(article);
				}


			if (articles.size() == 0) {
				System.out.println("게시글이 없습니다");
				continue;
			}

			System.out.println("번호   /   제목");

			for (Article article : articles) {
				System.out.printf("%4d   /   %s\n", article.id, article.title);
			}
		} else if (cmd.startsWith("article modify")) {
			System.out.println("==게시물 수정==");
			int id = Integer.parseInt(cmd.split(" ")[2]);

			System.out.printf("새 제목 : ");
			String newTitle = sc.nextLine();
			System.out.printf("새 내용 : ");
			String newBody = sc.nextLine();


				String sql = "UPDATE article";
				sql += " SET updateDate = NOW(),";
				sql += " title = '" + newTitle + "',";
				sql += " `body` = '" + newBody + "'";
				sql += " WHERE id = " + id + ";";


				pstmt = conn.prepareStatement(sql);

				pstmt.executeUpdate();

			System.out.println(id + "번 글이 수정되었습니다");
		}

	} catch (ClassNotFoundException e) {
		System.out.println("드라이버 로딩 실패");
	} catch (SQLException e) {
		System.out.println("에러 : " + e);
	} finally {
		try {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

	System.out.println("==프로그램 종료==");
	
	sc.close();
}
}
