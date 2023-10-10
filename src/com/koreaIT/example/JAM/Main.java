package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bean.UserBean;


public class Main {
	public static void main(String[] args) {
		System.out.println("=프로그램 시작==");
		Scanner sc = new Scanner(System.in);
		
//		JDBCInsertTest JDBCInsertTest =new JDBCInsertTest();
		
		List<Article> articles = new ArrayList<>();//db 대용으로 사용한 arraylist
		int lastArticleId = 0;

		while (true) {
			System.out.print("명령어 ) ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("article write")) {
				System.out.println("==게시물 작성==");
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();

				Article article = new Article(id, title, body);
				System.out.println(article);// 어떻게 들어가는지 보여주려는거.
				articles.add(article);//이게 list가 아니라 이제는 db 랑 연결이 되야하는거네
				
				insertJDBC(title, body);

				System.out.println(id + "번 글이 생성되었습니다");
				lastArticleId++;
			} else if (cmd.equals("article list")) {
				System.out.println("==게시물 목록==");

//				if (articles.size() == 0) {
//					System.out.println("게시글이 없습니다");
//					continue;
//				}

				System.out.println("번호   /   제목");
				
				showJDBC();
				
			}

		}

		System.out.println("==프로그램 종료==");
		sc.close();
	}

	private static void showJDBC() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Article id = null;
		Article title = null;
		Article body = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");
			System.out.println("연결 성공!");

//			실행 쿼리
			String sql = "select * from article";
			//Statement 생성 후 실행할 쿼리정보 등록
			Statement stmt = conn.createStatement();
			//결과를 담을 ResultSet 생성 후 결과 담기
			rs = stmt.executeQuery(sql);
			
			//결과를 담을 ArrayList생성
			List<Article> articles = new ArrayList<>();
			
			//결과를 담을 ArrayList생성
			ArrayList<UserBean> list = new ArrayList<UserBean>();
			
			//ResultSet에 담긴 결과를 ArrayList에 담기
			while(rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getString("ID"));
				bean.setName(rs.getString("title"));
				bean.setEmail(rs.getString("body"));
				list.add(bean);
			}
			//결과물 출력
			for(int i=list.size()-1; i>0; i--) {
				System.out.printf("%s   /   %s\n",list.get(i).getId(), list.get(i).getName());
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 : " + e);
		} finally {
			try {
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
	

	private static void insertJDBC(String title, String body) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");
			System.out.println("연결 성공!");

			String sql = "INSERT INTO article";
			sql += " SET regDate = NOW(),";
			sql += "updateDate = NOW(),";
			sql += "title = '"+ title +"', ";
			sql += "`body` = '"+ body +"'";

			System.out.println(sql);

			pstmt = conn.prepareStatement(sql);

			int affectedRow = pstmt.executeUpdate();

			System.out.println("affectedRow : " + affectedRow);

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러 : " + e);
		} finally {
			try {
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
}

