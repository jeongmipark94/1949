package admin.view;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import user.ee.controller.ModifyExtController;
import user.ee.view.EeInfoRegView;

public class ModifyExtView extends JDialog { /////////////////////// 0213 작업중(user꺼 복붙했음, 수정필요) /////

	private JTextField jtfPath;
	private JButton jbChoose, jbChange, jbCancel;

	public ModifyExtView(EeInfoRegView eirv) {
		super(eirv, "외부이력서 등록", true);

		JLabel jlMsg = new JLabel("외부 이력서 첨부");
		jlMsg.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		add(jlMsg);
		jlMsg.setBounds(10, 32, 250, 30);

		JLabel jlTemp = new JLabel("외부이력서는 doc, pdf만 첨부가능 합니다.");
		jlTemp.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		add(jlTemp);
		jlTemp.setBounds(80, 60, 250, 30);

		jtfPath = new JTextField();
		add(jtfPath);
		jtfPath.setBounds(120, 37, 140, 20);

		jbChoose = new JButton("파일선택");
		add(jbChoose);
		jbChoose.setBounds(270, 37, 90, 20);

		jbChange = new JButton("첨부하기");
		add(jbChange);
		jbChange.setBounds(75, 95, 100, 25);

		jbCancel = new JButton("취소");
		add(jbCancel);
		jbCancel.setBounds(190, 95, 100, 25);

		// 이벤트 등록
		ModifyExtController mec = new ModifyExtController(this, eirv);
		jbCancel.addActionListener(mec);
		jbChange.addActionListener(mec);
		jbChoose.addActionListener(mec);

		setLayout(null);
		setResizable(false);
		setBounds(eirv.getX() + 50, eirv.getY() + 150, 380, 170);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}// 생성자

	public JTextField getJtfPath() {
		return jtfPath;
	}

	public JButton getJbChoose() {
		return jbChoose;
	}

	public JButton getJbChange() {
		return jbChange;
	}

	public JButton getJbCancel() {
			return jbCancel;
		}