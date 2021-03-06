package user.er.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import user.er.controller.CoInfoRegController;
import user.util.JTextFieldLimit;

@SuppressWarnings("serial")
public class CoInfoRegView extends JDialog {
	private JButton jbReg, jbClose;
	private JTextField jtfCoName, jtfEstDate, memberNum;
	private JLabel jlImg1, jlImg2, jlImg3, jlImg4;
	private JTextArea jtaCoDesc;

	public CoInfoRegView(ErMainView emv, String erId) {
		super(emv, "회사상세정보",true);
		jtfCoName = new JTextField();
		jtfCoName.setDocument((new JTextFieldLimit(10)));
		
		jtfEstDate = new JTextField();
		jtfEstDate.setDocument((new JTextFieldLimit(10)));
		
		memberNum = new JTextField();
		memberNum.setDocument((new JTextFieldLimit(5)));
		memberNum.setText(String.valueOf(0));
		
		JLabel jlCoName = new JLabel("회사명");
		JLabel jlEstDate = new JLabel("설립년도");
		JLabel jlmemberNum = new JLabel(" 사원수");
		
		JLabel jlCoImgGuide=new JLabel("※이미지란을 클릭해서 이미지 등록이 가능합니다.");
		jlCoImgGuide.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		jlCoImgGuide.setForeground(new Color(0xFF0000));
		
		jtaCoDesc = new JTextArea();
		JScrollPane jspTaDesc = new JScrollPane(jtaCoDesc);
		
		jbReg = new JButton("등록");
		 jbClose = new JButton("닫기");
		
		jlImg1 = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/co/no_co_img1.png"));
		jlImg2 = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/co/no_co_img2.png"));
		jlImg3 = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/co/no_co_img3.png"));
		jlImg4 = new JLabel(new ImageIcon("C:/dev/1949/03.개발/src/user/img/co/no_co_img4.png"));
		
		//배치
		setLayout(null);
		
		jlCoName.setBounds(240, 46, 57, 26);
		jlEstDate.setBounds(240, 98, 57, 26);
		jlmemberNum.setBounds(240, 150, 57, 26);
		
		jtfCoName.setBounds(300, 46, 133, 29);
		jtfEstDate.setBounds(300, 98, 133, 29);
		memberNum.setBounds(300, 150, 133, 29);
		
		jlImg1.setBounds(38, 25, 170, 170);
		jlImg1.setBorder(new TitledBorder("회사 이미지"));
		jlImg2.setBounds(38, 208, 50, 50);
		jlImg3.setBounds(98, 208, 50, 50);
		jlImg4.setBounds(158, 208, 50, 50);
		
		jlImg2.setBorder(new LineBorder(Color.black));
		jlImg3.setBorder(new LineBorder(Color.black));
		jlImg4.setBorder(new LineBorder(Color.black));
		
		jlCoImgGuide.setBounds(30, 226, 300, 100);
		
		
		jspTaDesc.setBounds(32, 289, 405, 133);
		jspTaDesc.setBorder(new TitledBorder("기업 설명"));
		
		jbReg.setBounds(233, 446, 92, 24);
		jbClose.setBounds(342, 446, 92, 24);
		
		//이벤트 등록 
		CoInfoRegController crc= new CoInfoRegController(this, erId, emv);
		addWindowListener(crc);
		jbClose.addActionListener(crc);
		jbReg.addActionListener(crc);
		jlImg1.addMouseListener(crc);
		jlImg2.addMouseListener(crc);
		jlImg3.addMouseListener(crc);
		jlImg4.addMouseListener(crc);
		
		add(jlCoName);
		add(jlEstDate);
		add(jlmemberNum);
		
		add(jtfCoName);
		add(jtfEstDate);
		add(memberNum);
		
		add(jlImg1);
		add(jlImg2);
		add(jlImg3);
		add(jlImg4);
		
		add(jlCoImgGuide);
		
		add(jspTaDesc);
		
		add(jbReg);
		add(jbClose);
		
		setBounds(100, 100, 480, 540);
		setResizable(false);
		setVisible(true);
	}//생성자

	public JButton getJbReg() {
		return jbReg;
	}

	public JButton getJbClose() {
		return jbClose;
	}

	public JTextField getJtfCoName() {
		return jtfCoName;
	}

	public JTextField getJtfEstDate() {
		return jtfEstDate;
	}

	public JTextField getMemberNum() {
		return memberNum;
	}

	public JLabel getJlImg1() {
		return jlImg1;
	}

	public JLabel getJlImg2() {
		return jlImg2;
	}

	public JLabel getJlImg3() {
		return jlImg3;
	}

	public JLabel getJlImg4() {
		return jlImg4;
	}

	public JTextArea getJtaCoDesc() {
		return jtaCoDesc;
	}
}
