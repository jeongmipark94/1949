package admin.controller;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import admin.dao.AdminDAO;
import admin.view.AdminMgMtView;
import admin.view.EeModifyView;
import admin.view.ModifyExtView;
import admin.vo.EeInfoVO;
import admin.vo.EeModifyVO;

public class EeModifyController extends WindowAdapter implements ActionListener {

	private EeModifyView emv;
	private AdminMgMtView ammv;
	private AdminMgMtController ammc;
	private EeInfoVO eivo;
	
	private File changeImgFile; 
	private File changeExtFile;
	private boolean changeImgFlag;
	private boolean changeExtFlag;
	
	public EeModifyController(EeModifyView emv, AdminMgMtView ammv, AdminMgMtController ammc, EeInfoVO eivo) {
		this.emv = emv;
		this.ammv = ammv;
		this.ammc = ammc;
		this.eivo = eivo;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == emv.getJbCancel()) {
			emv.dispose();
			ammc.setEe();
		}
		
		if (e.getSource() == emv.getJbModify()) {
			try {
				modifyEe();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		if (e.getSource() == emv.getJbRemove()) {
			switch(JOptionPane.showConfirmDialog(emv, "기본정보를 정말 삭제하시겠습니까?")) {
			case JOptionPane.OK_OPTION:
				try {
					removeEe();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
		
		if (e.getSource() == emv.getJbChangeExt()) {
			changeExt();
		}
		
		if (e.getSource() == emv.getJbChangeImg()) {
			changeImg();
		}
 	}
	
	private Socket client;
	private DataOutputStream dos;
	private DataInputStream dis;
	private FileInputStream fis;
	private FileOutputStream fos;
	
	public void closeStreams() throws IOException {
		if (fos != null) {fos.close();}
		if (fis != null) {fis.close();}
		if (dos != null) { dos.close(); }
		if (dis != null) { dis.close(); }
		if (client != null) { client.close(); }
	}
	
	public void deleteFile(String fileName, String flag) throws UnknownHostException, IOException {
		client = new Socket("localhost", 7002);
		
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
		
		if (flag.equals("img")) {
			dos.writeUTF("eeImg_delete"); 
		} else if (flag.equals("ext")) {
			dos.writeUTF("ee_ext_delete");
		}
		dos.flush();
		
		dos.writeUTF(fileName);  // 기존 이미지명 전달
		dos.flush();
		
		dis.readUTF(); // 응답 후 연결 종료
		
		closeStreams();
	}
	
	public void addNewFile(File newFile, String flag) throws IOException {
		client = new Socket("localhost", 7002);
		
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
		
		if (flag.equals("img")) {
			dos.writeUTF("eeImg_register"); 
		} else if (flag.equals("ext")) {
			dos.writeUTF("ee_ext_register"); 
		}
		dos.flush();
		
		dos.writeUTF(newFile.getName()); // 새로운 이미지명 전달
		dos.flush();
		
		fis = new FileInputStream(newFile);
		
		byte[] readData = new byte[512];
		int len = 0;
		int arrCnt = 0;
		while((len = fis.read(readData)) != -1) {
			arrCnt++;
		}
		
		fis.close();

		dos.writeInt(arrCnt); // 파일의 크기 전송
		dos.flush();

		fis = new FileInputStream(newFile);
		
		len = 0;
		while((len = fis.read(readData)) != -1) {
			dos.write(readData, 0, len);
			dos.flush();
		}
		
		dis.readUTF(); // 저장완료 응답 받은 후 연결 종료
		closeStreams();
	}
	
	public void reqImg(String newFileName) throws IOException {
		client = new Socket("localhost", 7002);
		
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
		
		dos.writeUTF("eeImg_request");
		dos.flush();
		
		dos.writeUTF(newFileName);
		dos.flush();
		
		int arrCnt = dis.readInt();
		
		byte[] readData = new byte[512];
		int len = 0;
		
		fos = new FileOutputStream("C:/dev/1949/03.개발/src/admin/img/ee/"+newFileName);
		
		for(int i=0; i<arrCnt; i++) {
			len = dis.read(readData);
			fos.write(readData,0,len);
			fos.flush();
		}
		
		dos.writeUTF("done");
		dos.flush();
		
		closeStreams();
	}
	
	public void modifyEe() throws IOException {
		// 이미지, 이력서파일 변경되었는지 확인 - flag변수
		// 이미지가 변경되었다면 img패키지에 파일 전송(파일서버 완성 후 변경예정)
		// 이력서가 변경되었다면 기존 이력서파일 삭제 후 새로운 이력서파일 전송
		// 그 전에 변경 정보로 DB에서 update메소드처리
		
		String name = emv.getJtfName().getText().trim();
		
		if (name.equals("")) {
			msgCenter("이름을 입력해주세요.");
			emv.getJtfName().requestFocus();
			return;
		}

		String rank = emv.getJcbRank().getSelectedItem().equals("신입") ? "N" : "C";
		String loc = (String)emv.getJcbLoc().getSelectedItem();
		String education = (String)emv.getJcbEducation().getSelectedItem();
		String portfolio = emv.getJcbPortfolio().getSelectedItem().equals("YES") ? "Y" : "N";
		
		String img = eivo.getImg();
		if (changeImgFlag) { 
			img = changeImgFile.getName();
		}
		
		String extResume = eivo.getExtResume();
		if (changeExtFlag) {
			extResume = emv.getJtfExtRsm().getText().trim();
		}
		
		EeModifyVO emvo = new EeModifyVO(eivo.getEeNum(), img, rank, loc, education, portfolio, extResume);
		
		try {
			if(AdminDAO.getInstance().updateEe(emvo)) { // 데이터 수정완료
				
				if (changeImgFlag) {
					// ee는 no_img일리가 없으므로 삭제할 이미지가 no_img인지 체크 안해도 됨
					File originImg = new File("C:/dev/1949/03.개발/src/admin/img/ee/"+eivo.getImg());
					originImg.delete();
					deleteFile(eivo.getImg(), "img"); // 기존 이미지를 FS에서 삭제
					addNewFile(changeImgFile, "img"); // 새로운 이미지를 전송
					reqImg(changeImgFile.getName()); // 새로운 이미지를 FS에게 요청, 저장
				}
				
				if (changeExtFlag) { // 이력서는 Admin Server에 저장할 필요 없음
					// 기존 이력서를 FS에서 삭제
					deleteFile(eivo.getExtResume(), "ext");
					// 변경된 이력서만 FS에 추가 
					addNewFile(changeExtFile, "ext");
				}
				
				msgCenter("기본정보를 수정했습니다.");
				emv.dispose();
				
				EeInfoVO newEivo = AdminDAO.getInstance().selectOneEe(eivo.getEeNum());
				
				ammc.setEe();
				new EeModifyView(ammv, newEivo, ammc);
			}
		} catch (SQLException e) {
			msgCenter("DB에 문제 발생");
			e.printStackTrace();
		}
	}
	
	private void msgCenter(String msg) {
		JOptionPane.showMessageDialog(emv, msg);
	}
	
	public void removeEe() throws UnknownHostException, IOException {
		if(AdminDAO.getInstance().deleteEe(eivo)) {
			
			File originImg = new File("C:/dev/1949/03.개발/src/admin/img/ee/"+eivo.getImg());
			originImg.delete();
			deleteFile(eivo.getImg(), "img");
			
			msgCenter("기본정보가 삭제되었습니다.");
			emv.dispose();
			ammc.setEe();
		}
	}
	
	/**
	 * 이미지를 변경하는 메소드
	 * 변경 후 changeImgFlag를 true로 변경, changeImgFile을 생성
	 */
	public void changeImg() {
		// 이름의 경로를 저장, modifyEe가 수행될 때 이미지를 저장
		FileDialog fd = new FileDialog(emv, "이미지 변경", FileDialog.LOAD);
		fd.setVisible(true);
		
		if (fd.getDirectory() != null && fd.getName() != null) {
			if (fd.getFile().toLowerCase().endsWith(".png") || 
					fd.getFile().toLowerCase().endsWith(".jpg") || 
					fd.getFile().toLowerCase().endsWith(".jpeg") ||
					fd.getFile().toLowerCase().endsWith(".gif")) {
				changeImgFile = new File(fd.getDirectory()+fd.getFile());
				changeImgFlag = true;
				
				emv.getJlImg().setIcon(new ImageIcon(changeImgFile.getAbsolutePath()));
			} else {
				msgCenter("확장자가 png, jpg, jpeg, gif인 파일만 등록가능합니다.");
			}
		}
	}
	
	/**
	 * 이력서를 변경하는 메소드
	 */
	public void changeExt() {
		new ModifyExtView(emv, this);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		emv.dispose();
	}

	public File getChangeExtFile() {
		return changeExtFile;
	}

	public void setChangeExtFile(File changeExtFile) {
		this.changeExtFile = changeExtFile;
	}

	public boolean isChangeExtFlag() {
		return changeExtFlag;
	}
	public void setChangeExtFlag(boolean changeExtFlag) {
		this.changeExtFlag = changeExtFlag;
	}
}
