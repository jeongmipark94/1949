package user.common.view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import user.common.controller.RemoveUserController;

@SuppressWarnings("serial")
public class RemoveUserView extends JDialog {
	
	private JButton jbDelete, jbClose;
	private JPasswordField jpfPass1, jpfPass2;
	
	public RemoveUserView(ChangeUserInfoView cuiv, JFrame jf, String id) {
		super(jf, "1949 - ȸ�� Ż��", true);
		
		
		jpfPass1 = new JPasswordField(); 
		jpfPass2 = new JPasswordField();
		jbDelete = new JButton("Ż��");
		jbClose = new JButton("�ݱ�");
		
		JLabel jlTitle1 = new JLabel("����Ͻ� ������ ��� �����˴ϴ�.");
		JLabel jlTitle2 = new JLabel("���� Ż���Ͻðڽ��ϱ�?");
		JLabel jlPass1 = new JLabel("��й�ȣ");
		JLabel jlPass2 = new JLabel("��й�ȣ");
		JLabel jlPass3 = new JLabel("Ȯ��");
		
		setLayout(null);
		
		jlTitle1.setBounds(100, 20, 200, 30);
		jlTitle2.setBounds(125, 40, 180, 30);
		jlPass1.setBounds(50, 90, 80, 30);
		jlPass2.setBounds(50, 125, 80, 30);
		jlPass3.setBounds(63, 140, 80, 30);
		jpfPass1.setBounds(120, 90, 210, 30);
		jpfPass2.setBounds(120, 130, 210, 30);
		jbDelete.setBounds(100, 185, 90, 30);
		jbClose.setBounds(200, 185, 90, 30);
		
		add(jlTitle1);
		add(jlTitle2);
		add(jlPass1);
		add(jlPass2);
		add(jlPass3);
		add(jpfPass1);
		add(jpfPass2);
		add(jbDelete);
		add(jbClose);
		
		RemoveUserController ruc = new RemoveUserController(jf, cuiv,this, id);
		jbDelete.addActionListener(ruc);
		jbClose.addActionListener(ruc);
		jpfPass1.addKeyListener(ruc);
		jpfPass2.addKeyListener(ruc);
		
		addWindowListener(ruc);
		
		//setBounds(500, 200, 400, 280);
		setBounds(jf.getX()+50,jf.getY()+50,400,280);
		setResizable(false);
		setVisible(true);
	}
	
/*	public static void main(String[] args) {
		new RemoveUserView(emv, id);
	}*/
	public JButton getJbDelete() {
		return jbDelete;
	}
	public JButton getJbClose() {
		return jbClose;
	}
	public JPasswordField getJpfPass1() {
		return jpfPass1;
	}
	public JPasswordField getJpfPass2() {
		return jpfPass2;
	}
}
