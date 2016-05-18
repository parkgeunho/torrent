package com.util;

public class MyUtil {	// ����¡ ó��
	
	// ��ü������ ��
	// numPerPage :  �� ȭ�鿡 ǥ���� ������ ����
	// dataCount : ��ü ������ ����
	public int getPageCount(int numPerPage, int dataCount){
		
		int pageCount = 0;
		
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0)
			pageCount++;
		
		return pageCount;
	}
	
	// ����¡ ó�� �޼ҵ�
	// currentPage : ���� ǥ���� ������ ��ȣ
	// totalPage : ��ü ������ ��
	// listUrl : ��ũ�� ������ url(list.jsp)
	public String pageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 5;  // ������ 6 7 8 9 10 ������
		int currentPageSetup; // ������ư�� ����
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage == 0 || totalPage == 0)
			return "";
		
		// listUrl		
		// list.jsp? --> pageNum=9
		// list.jsp?searchKey=name&searchValue='suzi'& --> pageNum=9
		if(listUrl.indexOf("?") != -1)
			listUrl = listUrl + "&";
		else
			listUrl = listUrl + "?";
		
		/*//currentPageSetup
		currentPageSetup = (currentPage-1/numPerBlock)*numPerBlock;*/
		
		//currentPageSetup
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		
		// ������ 6 7 8 9 10 ������
		
		// ������
		if(/*totalPage>numPerBlock &&*/ currentPageSetup>0){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" +
						currentPageSetup + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=5">������</a>&nbsp;
		}
		
		// �ٷΰ��� ������(6 7 8 9 10)
		page = currentPageSetup + 1;
		
		while(page<=totalPage && page<=(currentPageSetup + numPerBlock)){
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">9</font>&nbsp;
			}else{
				
				sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=6">6</a>&nbsp;
			}
			
			page++;
		}
		
		// ������
		if(totalPage-currentPageSetup > numPerBlock){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum=" + page + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">������</a>&nbsp;
		}
		
		return sb.toString();
	}
	
public String reportPageIndexList(int currentPage, int totalPage, String listUrl){
		
		int numPerBlock = 5;  // ������ 6 7 8 9 10 ������
		int currentPageSetup; // ������ư�� ����
		int page;
		
		StringBuffer sb = new StringBuffer();
		
		if(currentPage == 0 || totalPage == 0)
			return "";
		
		// listUrl		
		// list.jsp? --> pageNum=9
		// list.jsp?searchKey=name&searchValue='suzi'& --> pageNum=9
		if(listUrl.indexOf("?") != -1)
			listUrl = listUrl + "&";
		else
			listUrl = listUrl + "?";
		
		/*//currentPageSetup
		currentPageSetup = (currentPage-1/numPerBlock)*numPerBlock;*/
		
		//currentPageSetup
		currentPageSetup = (currentPage/numPerBlock)*numPerBlock;
		
		if(currentPage % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		
		// ������ 6 7 8 9 10 ������
		
		// ������
		if(/*totalPage>numPerBlock &&*/ currentPageSetup>0){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" +
						currentPageSetup + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=5">������</a>&nbsp;
		}
		
		// �ٷΰ��� ������(6 7 8 9 10)
		page = currentPageSetup + 1;
		
		while(page<=totalPage && page<=(currentPageSetup + numPerBlock)){
			
			if(page == currentPage){
				
				sb.append("<font color=\"Fuchsia\">" + page + "</font>&nbsp;");
				//<font color = "Fuchsia">9</font>&nbsp;
			}else{
				
				sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" + page + "\">" + page + "</a>&nbsp;");
				//<a href="list.jsp?pageNum=6">6</a>&nbsp;
			}
			
			page++;
		}
		
		// ������
		if(totalPage-currentPageSetup > numPerBlock){
			
			sb.append("<a style=\"color:black; text-decoration: none;\" href=\"" + listUrl + "pageNum1=" + page + "\">������</a>&nbsp;");
			//<a href="list.jsp?pageNum=11">������</a>&nbsp;
		}
		
		return sb.toString();
	}
	
	
	
}


















