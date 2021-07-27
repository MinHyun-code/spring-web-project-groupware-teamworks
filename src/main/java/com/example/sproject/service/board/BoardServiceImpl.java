package com.example.sproject.service.board;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sproject.dao.board.BoardDao;
import com.example.sproject.model.board.Post;
import com.example.sproject.model.board.Reply;
import com.example.sproject.model.common.CommonGroup;
import com.example.sproject.model.board.Board;


@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;
	private List<Board> boardList;
	private Object replyList;
	private int rp_num;


	@Override
	public List<Post> listPost(Post post) {
	List<Post> postList = null;
	System.out.println("PostServiceImpl listPost Start...");
	postList =boardDao.listPost(post);
		return postList;
	}

	@Override
	public int total() {
		System.out.println("PostServiceImpl Start total...");
		int totCnt = boardDao.total();
		System.out.println("PostServiceImpl total totCnt->"+totCnt);
		return totCnt;
	}
	// p_num, p_name, p_content, p_regdate, p_view
	@Override
	public int insert(Post post) {
	System.out.println("PostServiceImpl Start insert");
		String m_id =post.getM_id();
		String p_name = post.getP_name();
        String p_content = post.getP_content();
        // *태그문자 처리 (< ==> &lt; > ==> &gt;)
        // replace(A, B) A를 B로 변경
        p_name = p_name.replace("<", "&lt;");
        p_name = p_name.replace("<", "&gt;");
        m_id = m_id.replace("<", "&lt;");
        m_id = m_id.replace("<", "&gt;");
        // *공백문자 처리
        p_name = p_name.replace("  ",    "&nbsp;&nbsp;");
        m_id = m_id.replace("  ",    "&nbsp;&nbsp;");
        // *줄바꿈 문자처리
        p_content = p_content.replace("\n", "<br>");
        post.setM_id(m_id);
        post.setP_name(p_name);
        post.setP_content(p_content);
        
        return boardDao.insert(post);

}


	@Override
	public Post read(int p_num) {
		System.out.println("PostServiceImpl Start read...");
		return boardDao.read(p_num);
	}

	@Override
	public void increase_p_view(int p_num, HttpSession session) {
		long update_time = 0;
        // 세션에 저장된 조회시간 검색
        // 최초로 조회할 경우 세션에 저장된 값이 없기 때문에 if문은 실행X
        if(session.getAttribute("update_time_"+p_num) != null){
                                // 세션에서 읽어오기
            update_time = (long)session.getAttribute("update_time_"+p_num);
        }
        // 시스템의 현재시간을 current_time에 저장
        long current_time = System.currentTimeMillis();
        // 일정시간이 경과 후 조회수 증가 처리 24*60*60*1000(24시간)
        // 시스템현재시간 - 열람시간 > 일정시간(조회수 증가가 가능하도록 지정한 시간)
        System.out.println("current_time - update_time: " + (current_time - update_time));
        if(current_time - update_time > 24*60*60*1000){
        	//조회수를 증가시키는 로직
            boardDao.increase_p_view(p_num);
            // 세션에 시간을 저장 : "update_time_"+p_num는 다른변수와 중복되지 않게 명명한 것
            // 키 : update_time_{p_num}, 값 : {current_time}
            session.setAttribute("update_time_"+p_num, current_time);
         
	}
        
	
	}

	@Override
	public int update(Post post) {
		System.out.println("PostServiceImpl Start update...");
		int result = 0;
		result = boardDao.update(post);
		return result;
	
	}

	@Override
	public int delete(int p_num) {
		System.out.println("PostServiceImpl Start update...");
		int result = 0;
		//해당 스크랩 제거 추후  postLikeDao 생성후 수정하게
	    boardDao.likeDelete(p_num);
	
		result = boardDao.delete(p_num);
		return result;
	}

	@Override
	public List<Board> listBoard(Board board) {
		System.out.println("PostServiceImpl listBoard Start...");
		boardList = boardDao.listBoard(board);
		return boardList;
	}

	@Override
	public List<Reply> listReply(int p_num) {
		System.out.println("PostServiceImpl listReply Start...");
		List<Reply>replyList = boardDao.listReply(p_num);
		return replyList;
	}

	@Override
	public int insert(Reply reply) {
		System.out.println("PostServiceImpl Start replyinsert");
		System.out.println("reply: " + reply.toString());
		int rp_num = 1 + boardDao.selectOneMaxRp_num();
		reply.setRp_num(rp_num);
		return boardDao.insert(reply);
	}
	@Override
	public int reply_delete(int rp_num) {
		System.out.println("PostServiceImpl Start replydelete...");
		int result = 0;
		result = boardDao.reply_delete(rp_num);
		return result;
	}

	@Override
	public int rereply_insert(Reply reply, int p_num, int parent_rp_num) {
	
	reply.setP_num(p_num);
	
	reply.setRp_num(parent_rp_num);
	Reply parentReply = boardDao.selectOneParentReply(reply);
	reply.setParent_rp_ref(parentReply.getRp_ref());
	reply.setRp_order(parentReply.getRp_order());
	reply.setRp_depth(parentReply.getRp_depth());


	
	//tRp_ref, Rp_depth 값 설정
	reply.setRp_ref(reply.getParent_rp_ref());
	reply.setRp_depth(reply.getRp_depth() + 1);
	
	//삽입될 cg_num 찾기
	int rp_num = boardDao.selectOneMaxRp_num(p_num) + 1;
	reply.setRp_num(rp_num);
	//삽입될 cg_order 값 찾기
			int insertedRp_order = boardDao.selectOneInsertedRp_order(reply);
			if (insertedRp_order < 0) { //해당 ref에서 사이에 삽입되는 것이 아닌 제일 뒤에 삽입되는 경우
				insertedRp_order = boardDao.selectOneMaxRp_order(reply) + 1;
			}
			reply.setRp_order(insertedRp_order);
	//기존 reply들 order 한칸씩 뒤로밀어버리기
			boardDao.pushRp_order(reply);
			//CommonGroup 삽입하기 전 null값 처리하기
			if(reply.getM_id() ==null) reply.setM_id("");
			if(reply.getRp_content() ==null) reply.setRp_content("");
			
			return boardDao.rereply_insert(reply);

}
}

        
