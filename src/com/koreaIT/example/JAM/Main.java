package com.koreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("=프로그램 시작==");
		Scanner sc = new Scanner(System.in);

//		List<Article> articles = new ArrayList<>();
//		int lastArticleId = 0;

		while (true) {
			System.out.print("명령어 ) ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}

			if (cmd.equals("article write")) {
				System.out.println("==게시물 작성==");
//				int id = lastArticleId + 1;

				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();

//				Article article = new Article(id, title, body);
				Article article = new Article(title, body);
//				System.out.println(article);
//				articles.add(article);

//				System.out.println(id + "번 글이 생성되었습니다");

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
					sql += "title = '" + title + "',";
					sql += "`body` = '" + body + "';";

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

//				lastArticleId++;
			} else if (cmd.equals("article list")) {
				System.out.println("==게시물 목록==");

				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				// 결과를 담을 ArrayList생성
				ArrayList<Article> articles = new ArrayList<Article>();

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "SELECT *";
					sql += " FROM article";
					sql += " ORDER BY id DESC;";

					System.out.println(sql);

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

				System.out.println(articles);

				if (articles.size() == 0) {
					System.out.println("게시글이 없습니다");
					continue;
				}

				System.out.println("번호   /   제목");

				for (Article article : articles) {
					System.out.printf("%4d   /   %s\n", article.id, article.title);
				}
			}else if (cmd.startsWith("article modify")) {

				String[] commandDiv = cmd.split(" ");

				int id = Integer.parseInt(commandDiv[2]);

				
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				
				Article article = new Article(title, body);

				Connection conn = null;
				PreparedStatement pstmt = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "UPDATE article";
					sql += " SET title = '"+title+"', ";
					sql += "`body` = '"+body+"',";
					sql += " updateDate = NOW()";
					sql += " WHERE id = "+id+";";

					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

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


			}else if (cmd.startsWith("article delete")) {

				String[] commandDiv = cmd.split(" "); // article delete 1

				int id = Integer.parseInt(commandDiv[2]);
				
				Connection conn = null;
				PreparedStatement pstmt = null;

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "DELETE FROM article";
					sql += " WHERE id = "+id+";";

					System.out.println(sql);

					pstmt = conn.prepareStatement(sql);

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

				System.out.println(id + "번 글을 삭제했어");

			} else if (cmd.startsWith("article detail")) {

				String[] commandDiv = cmd.split(" "); // article detail 1

				int id = Integer.parseInt(commandDiv[2]);

				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;

				// 결과를 담을 ArrayList생성
				ArrayList<Article> articles = new ArrayList<Article>();

				try {
					Class.forName("com.mysql.jdbc.Driver");
					String url = "jdbc:mysql://127.0.0.1:3306/JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");
					System.out.println("연결 성공!");

					String sql = "SELECT *";
					sql += " FROM article";
					sql += " WHERE id=2";

					pstmt = conn.prepareStatement(sql);

					// 결과를 담을 ResultSet 생성 후 결과 담기
					rs = pstmt.executeQuery(sql);

					// ResultSet에 담긴 결과를 ArrayList에 담기
					while (rs.next()) {
						id = rs.getInt("id");
						String regDate = rs.getString("regDate");
						String updateDate = rs.getString("updateDate");
						String title = rs.getString("title");
						String body = rs.getString("body");

						Article article = new Article(id, regDate, updateDate, title, body);

						articles.add(article);
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

				System.out.println(articles);
			} else {
				System.out.println("존재하지 않는 명령어입니다");
				continue;
			}

		}

		System.out.println("==프로그램 종료==");
		sc.close();
	}
}
