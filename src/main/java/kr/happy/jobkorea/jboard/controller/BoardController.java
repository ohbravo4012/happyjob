package kr.happy.jobkorea.jboard.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.happy.jobkorea.jboard.service.BoardService;
import kr.happy.jobkorea.mss.service.MssService;

@Controller
@RequestMapping("/jboard/")
public class BoardController {

	// Set logger
			private final Logger logger = LogManager.getLogger(this.getClass());

			// Get class name for loggerBoardController.java
			
			@Autowired
			BoardService boardService;
			
			
			@RequestMapping("/room.do")
			public String jboard(){

				logger.info("+ Start adminNotice");

				
				
				logger.info("+ End adminNotice");
				return "/jboard/Room";
			}
			
			@RequestMapping("roomList.do")
			@ResponseBody
			public Map<String,Object> roomList(
													Model model, 
													@RequestParam Map<String, Object> paramMap, 
													HttpServletRequest request,
													HttpServletResponse response, 
													HttpSession session	){
				
				logger.info("+ Start noticeList");
				logger.info("-- paramMap : "+paramMap);

				int currentPage = Integer.parseInt((String)paramMap.get("currentPage"));	// 현재 페이지 번호
				int pageSize = Integer.parseInt((String)paramMap.get("pageSize"));			// 페이지 사이즈
				int pageIndex = (currentPage-1)*pageSize;		
				
				paramMap.put("pageIndex", pageIndex);
				paramMap.put("pageSize", pageSize);
				
				// 자료게시판 조회
				
				List<Map<String,Object>> list = boardService.getRoomList(paramMap);
				
				Map<String,Object> resultMap=new HashMap<String, Object>();
				resultMap.put("list", list);
				
				//공지사항 총수.
				int totalCount = boardService.totalCountRoom(paramMap);
				resultMap.put("totalCountList", totalCount);
				resultMap.put("pageSize", pageSize);
				resultMap.put("currentPageNoticeList",currentPage);
				
				logger.info("+ resultMap : " +resultMap);
				logger.info("+ End noticeList");
				
				
				return resultMap;
			}

			@RequestMapping("updateNotice.do")
			@ResponseBody
			public Map<String,Object> updateNotice(
												Model model,
												@RequestParam Map<String,Object> paramMap,
												HttpServletRequest request,
												HttpServletResponse response, 
												HttpSession session ){
				
				logger.info("+ Start updateNotice");
				logger.info("-- paramMap : "+paramMap);
				
				
				String action = (String)paramMap.get("action");
				String msg = "";
				
				try {
					if(action.equals("I")){
						logger.info("추가 실행.");
						boardService.insertNotice(paramMap, request);
						msg="추가 성공";
						
					}else if(action.equals("U")){
						logger.info("수정 실행.");
						boardService.updateNotice(paramMap, request);
						msg="수정 성공";
					}else if(action.equals("D")){
						logger.info("삭제 실행.");
						boardService.deleteNotice(paramMap,request);
						msg="삭제 성공";
					}else{
						msg=action+" 이게맞아?";
					}
				} catch (Exception e) {
					e.printStackTrace();
					msg="에러남..허허허";
				}
				
				
				logger.info("msg : " + msg);
				
				Map<String,Object> resultMap=new HashMap<String, Object>();
				
				resultMap.put("msg",msg);
				
				
				
				logger.info("+ End updateNotice");
				
				return resultMap;
			}

			@RequestMapping("gereplyNcntUp.do")
			@ResponseBody
			public Map<String,Object> gereplyNcntUp(
												Model model,
												@RequestParam Map<String,Object> paramMap,
												HttpServletRequest request,
												HttpServletResponse response, 
												HttpSession session ){
				
				logger.info("+ Start gereplyNcntUp");
				logger.info("-- paramMap : "+paramMap);
				
				//  카운트 증가하고 카운트 가져오기 .
				String nt_seq = (String)paramMap.get("nt_seq");
				boardService.countUp(nt_seq);
				//댓글 리스트 가져오기 
				//List<Map<String,Object>> rList = mssService.getListReply(paramMap); 
				
				Map<String,Object> resultMap=new HashMap<String, Object>();
				
				//resultMap.put("rList", rList);
				//resultMap.put("cnt", cnt);
				
				logger.info("+ End gereplyNcntUp");
				
				return resultMap;
			}
			
			@RequestMapping("downloadQnaFile.do")
			public void downloadQnaFile(Model model, @RequestParam Map<String, Object> paramMap, HttpServletRequest request,
					HttpServletResponse response, HttpSession session) throws Exception {
			
				logger.info("+ Start Mss downloadQnaFile");
				logger.info("   - paramMap : " + paramMap);
				
				// 첨부파일 조회
				Map<String,Object> list=boardService.selectNoticeOne(paramMap);
				
				
				
				byte fileByte[] = FileUtils.readFileToByteArray(new File((String)list.get("file_path")));
				
				response.setContentType("application/octet-stream");
			    response.setContentLength(fileByte.length);
			    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode((String)list.get("file_name"),"UTF-8")+"\";");
			    response.setHeader("Content-Transfer-Encoding", "binary");
			    response.getOutputStream().write(fileByte);
			     
			    response.getOutputStream().flush();
			    response.getOutputStream().close();

				logger.info("+ End Mss downloadQnaFile");
			}
	
			
}
