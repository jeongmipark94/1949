package user.er.controller;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import user.common.vo.ErMainVO;
import user.dao.CommonDAO;
import user.dao.ErDAO;
import user.er.view.CoInfoRegView;
import user.er.view.ErMainView;
import user.er.vo.CoInsertVO;
import user.util.UserLog;
import user.util.UserUtil;

public class CoInfoRegController extends WindowAdapter implements MouseListener, ActionListener {

	private CoInfoRegView cirv;
	private String erId;

	private File uploadImg1, uploadImg2, uploadImg3, uploadImg4;
	private boolean imgFlag1, imgFlag2, imgFlag3, imgFlag4;
	private UserUtil uu;
	private UserLog ul;
	private String path, name;
	private ErDAO erdao;
	private ErMainView emv;

	public CoInfoRegController(CoInfoRegView cirv, String erId, ErMainView emv) {
		this.cirv = cirv;
		this.erId = erId;
		this.emv = emv;
		erdao = ErDAO.getInstance();

		uu = new UserUtil();
		ul = new UserLog();
	}// 생성자

	@Override
	public void windowClosing(WindowEvent e) {
		cirv.dispose();
	}// 윈도우 닫기

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == cirv.getJbReg()) {
			try {
				register();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(cirv, "DB오류");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (ae.getSource() == cirv.getJbClose()) {
			cirv.dispose();
		}
	}// 버튼 동작

	@Override
	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == cirv.getJlImg1()) {
			chgImg(cirv.getJlImg1(), 1);
		} // end if

		if (me.getSource() == cirv.getJlImg2()) {
			chgImg(cirv.getJlImg2(), 2);
		} // end if

		if (me.getSource() == cirv.getJlImg3()) {
			chgImg(cirv.getJlImg3(), 3);
		} // end if

		if (me.getSource() == cirv.getJlImg4()) {
			chgImg(cirv.getJlImg4(), 4);
		} // end if

	}// mouseClicked
	
	/**
	 * 설립일 검증 메소드
	 * @param estDate
	 * @return
	 */
	private boolean chkEstDate(String estDate) {
		boolean flag = false;

		String number = estDate.replaceAll("-", "");

		if (number.length() != 8 ) { // 0000-00-00
			return flag;
		}
		
		int yyyy = 0;
		int mm = 0;
		int dd = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int currYear = Integer.parseInt(sdf.format(new Date()));
		
		try {
			Integer.parseInt(number);
			yyyy = Integer.parseInt(number.substring(0, 4));
			mm = Integer.parseInt(number.substring(4,6));
			dd = Integer.parseInt(number.substring(6,8));
			
			if (yyyy > currYear) { // 설립년도가 올해보다 클 수 없음
				return flag;
			}
			
			if (mm > 12 || mm < 1) { // 월은 0보다 작거나 12보다 클 수 없음
				return flag;
			}

			if (dd > 31 || dd < 1) { // 일은 0보다 작거나 31보다 클 수 없으
				return flag;
			}
			
			flag = true;
		} catch (NumberFormatException npe) {
			flag = false;
		}
		
		return flag;
	}

	public void register() throws SQLException, IOException {
//		boolean insertFlag = false;
		if (uploadImg1 == null) {
			JOptionPane.showMessageDialog(cirv, "회사로고는 필수 사항입니다.");
			return;
		} // end if

		String coName = cirv.getJtfCoName().getText().trim();
		String estDate = cirv.getJtfEstDate().getText().trim();
		String coDesc = cirv.getJtaCoDesc().getText().trim();

		if (coName.equals("")) {
			JOptionPane.showMessageDialog(cirv, "회사명을 입력해주세요.");
			return;
		} // end if

		if (estDate.isEmpty()) {
			JOptionPane.showMessageDialog(cirv, "설립일을 입력해주세요.");
			return;
		} // end if
		
		if (!chkEstDate(estDate)) {
			JOptionPane.showMessageDialog(cirv, "설립년도의 입력형식을 아래와 같은 형식으로 해주세요\\nex)19901217\\nex)1990-12-17");
			cirv.getJtfEstDate().setText("");
			cirv.getJtfEstDate().requestFocus();
			return;
		}

		int memberNum = 0;
		try {
			memberNum = Integer.parseInt(cirv.getMemberNum().getText().trim());

			if (memberNum == 0) {
				JOptionPane.showMessageDialog(cirv, "사원수를 입력해주세요.");
				return;
			} // end if

		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(cirv, " 사원수는 숫자만 입력 가능합니다.");
			return;
		} // end catch

		if ("".equals(coDesc)) {
			JOptionPane.showMessageDialog(cirv, "기업 설명을 입력해주세요!");
			return;
		}

		String imgName1 = "";
		String imgName2 = "";
		String imgName3 = "";
		String imgName4 = "";
		
		// 회사로고는 필수항목이므로 null일리가 없음(위에서 예외처리)
		imgName1 = System.currentTimeMillis()+uploadImg1.getName();

		if (uploadImg2 == null) {
			uploadImg2 = new File("C:/dev/1949/03.개발/src/user/img/co/no_co_img2.png");
			imgName2 = "no_co_img2.png";
		} else {
			imgName2 = System.currentTimeMillis()+uploadImg2.getName();
		}
		
		if (uploadImg3 == null) {
			uploadImg3 = new File("C:/dev/1949/03.개발/src/user/img/co/no_co_img3.png");
			imgName3 = "no_co_img3.png";
		} else {
			imgName3 = System.currentTimeMillis()+uploadImg3.getName();
		}
		
		if (uploadImg4 == null) {
			uploadImg4 = new File("C:/dev/1949/03.개발/src/user/img/co/no_co_img4.png");
			imgName4 = "no_co_img4.png";
		} else {
			imgName4 = System.currentTimeMillis()+uploadImg4.getName();
		}
		
		CoInsertVO civo = null;
		civo = new CoInsertVO(erId, imgName1, imgName2, imgName3,
				imgName4, coName, estDate, coDesc, memberNum);
		
//		ActivationVO avo = new ActivationVO(erId);
		
		
		if (erdao.updateErInfo(civo)) {
			JOptionPane.showMessageDialog(cirv, "회사가 등록되었습니다.");
		
			Socket client = null;
			DataOutputStream dos = null;
			DataInputStream dis = null;
			FileInputStream fis = null;
			FileOutputStream fos = null;
			
			/////////// 이미지가 변경됐다면 FS에 등록작업 수행 // 영근0302
			if (imgFlag1) {
				uu.addNewFile(imgName1, uploadImg1, "co", client, dos, dis, fis);
				uu.reqFile(imgName1, "co", client, dos, dis, fos);
			}
			
			if (imgFlag2) {
				uu.addNewFile(imgName2, uploadImg2, "co", client, dos, dis, fis);
				uu.reqFile(imgName2, "co", client, dos, dis, fos);
			}
			
			if (imgFlag3) {
				uu.addNewFile(imgName3, uploadImg3, "co", client, dos, dis, fis);
				uu.reqFile(imgName3, "co", client, dos, dis, fos);
			}
			
			if (imgFlag4) {
				uu.addNewFile(imgName3, uploadImg3, "co", client, dos, dis, fis);
				uu.reqFile(imgName3, "co", client, dos, dis, fos);
			}
			
			JOptionPane.showMessageDialog(cirv, "회사 정보가 등록되었습니다\n이제부터 구직 정보를 조회가능합니다.");
			ErMainVO updateEmvo = CommonDAO.getInstance().selectErMain(erId, "Y");
			String coNum=erdao.selectCoNum(erId);
			ul.sendLog(erId, "["+coNum+"] 등록");
			new ErMainView(updateEmvo);
			emv.dispose();
			cirv.dispose();
		} else {
			JOptionPane.showMessageDialog(cirv, "회사 등록 실패(DB 문제");
		}
	}// register

	public void chgImg(JLabel jl, int imgNumber) {
		boolean flag = false;
		FileDialog fd = new FileDialog(cirv, "이미지 선택", FileDialog.LOAD);
		fd.setVisible(true);

		path = fd.getDirectory();
		name = fd.getFile();
		
		if (path == null || name == null) {
			return;
		}
		
		String[] extFlag = { "jpg", "jpeg", "gif" };
		for (String ext : extFlag) {
			if (name.toLowerCase().endsWith(ext)) {
				flag = true;
			} // end if
		} // end for

		if (flag) {
			if (imgNumber == 1) {
				uploadImg1 = new File(path + name);
				imgFlag1 = true;
				cirv.getJlImg1().setIcon(new ImageIcon(uploadImg1.getAbsolutePath()));
			} else if (imgNumber == 2) {
				uploadImg2 = new File(path + name);
				imgFlag2 = true;
				cirv.getJlImg2().setIcon(new ImageIcon(uploadImg2.getAbsolutePath()));
			} else if (imgNumber == 3) {
				uploadImg3 = new File(path + name);
				imgFlag3 = true;
				cirv.getJlImg3().setIcon(new ImageIcon(uploadImg3.getAbsolutePath()));
			} else if (imgNumber == 4) {
				uploadImg4 = new File(path + name);
				imgFlag4 = true;
				cirv.getJlImg4().setIcon(new ImageIcon(uploadImg4.getAbsolutePath()));
			} // end else if
		} else {
			JOptionPane.showMessageDialog(cirv, name + "파일은 이미지형식이 아닙니다.\n확장자가 jpg, jpeg, gif파일만 등록가능합니다.");
		} // end else
	}// chgImg

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}