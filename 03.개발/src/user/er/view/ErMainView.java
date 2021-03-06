package user.er.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import user.common.vo.ErMainVO;
import user.er.controller.ErMainController;

@SuppressWarnings("serial")
public class ErMainView extends JFrame {

	private JButton jbCoMgmt, jbEeInfo, jbErMgmt, jbApp, jbInterestEe;
	private JLabel jlUserInfo, jlLogOut, jlActivation;
	
	public ErMainView(ErMainVO ermvo) {
		super("1949 - 기업사용자 ["+ermvo.getName()+"]");
		
		JLabel jlAct = new JLabel("회사정보 등록여부 : ");
		JLabel jlImg = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/co/"+ermvo.getImg1()));
		jlImg.setBorder(new TitledBorder("회사 이미지"));
		
		jlActivation = new JLabel(ermvo.getActivation());
		jlUserInfo = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/userInfoMgmt.png"));
		JLabel jlUserMsg=new JLabel("회원정보관리");
		add(jlUserMsg);
		jlLogOut = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/logout.png"));
		JLabel jlLogOutMsg=new JLabel("로그아웃");
		add(jlLogOutMsg);
		jbCoMgmt = new JButton("회사정보 관리");
		jbEeInfo = new JButton("구직정보 보기");
		jbErMgmt = new JButton("구인정보 관리");
		jbApp = new JButton("지원현황");
		jbInterestEe = new JButton("관심 구직자 관리");
		
		setLayout(null);
		
		jlAct.setBounds(20, 20, 150, 30);
		jlActivation.setBounds(145,20,30,30);
		jlImg.setBounds(15, 70, 210, 180);
		jlUserInfo.setBounds(260, 15, 100, 30); 
		jlUserMsg.setBounds(268, 40, 100, 30);
		jlLogOut.setBounds(358, 15, 100, 30); 
		jlLogOutMsg.setBounds(380, 40, 100, 30);
		jbCoMgmt.setBounds(250, 70, 200, 50);
		jbEeInfo.setBounds(250, 140, 200, 50);
		jbErMgmt.setBounds(250, 210, 200, 50);
		jbApp.setBounds(250, 280, 200, 50);
		jbInterestEe.setBounds(250, 350, 200, 50);
		
		add(jlAct);
		add(jlActivation);
		add(jlImg);
		
		add(jlUserInfo);
		add(jlLogOut);
		add(jbCoMgmt);
		add(jbEeInfo);
		add(jbErMgmt);
		add(jbApp);
		add(jbInterestEe);
		
		ErMainController emc = new ErMainController(this ,ermvo);
		jbCoMgmt.addActionListener(emc);
		jbEeInfo.addActionListener(emc);
		jbErMgmt.addActionListener(emc);
		jbApp.addActionListener(emc);
		jbInterestEe.addActionListener(emc);
		jlUserInfo.addMouseListener(emc);
		jlLogOut.addMouseListener(emc);
		
		addWindowListener(emc);

		setBounds(500, 200, 475, 450);
		setResizable(false);
		setVisible(true);
	}//생성자
	
	public JButton getJbCoMgmt() {
		return jbCoMgmt;
	}
	public JButton getJbEeInfo() {
		return jbEeInfo;
	}
	public JButton getJbErMgmt() {
		return jbErMgmt;
	}
	public JButton getJbApp() {
		return jbApp;
	}
	public JButton getJbInterestEe() {
		return jbInterestEe;
	}
	public JLabel getJlUserInfo() {
		return jlUserInfo;
	}
	public JLabel getJlLogOut() {
		return jlLogOut;
	}
	public JLabel getJlActivation() {
		return jlActivation;
	}
	
}//class
