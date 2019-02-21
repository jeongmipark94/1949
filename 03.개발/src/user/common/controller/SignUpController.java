package user.common.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import user.common.view.SearchAddrView;
import user.common.view.SignUpView;
import user.common.vo.UserInsertVO;
import user.dao.CommonDAO;

public class SignUpController extends WindowAdapter implements ActionListener {
     private SignUpView suv;
     private String addrSeq;
     private CommonDAO c_dao;
    
     public SignUpController(SignUpView suv) {
          this.suv=suv;
          c_dao=CommonDAO.getInstance();
     }//생성자

     @Override
     public void windowClosing(WindowEvent e) {
          suv.dispose();
     }//closing
    
     @Override
     public void actionPerformed(ActionEvent ae) {
          if(ae.getSource()==suv.getJbSignUp()) {//가입 버튼이 눌려졌을 때 다 입력받기

               signUp();
          }else if(ae.getSource()==suv.getJbAddr()) {
               new SearchAddrView(suv, this, null); //null은 혜원이 부분 changeuser info 부분에 
          }else if(ae.getSource()==suv.getJbCancel()) {
               suv.dispose();
          }//end if

     }//버튼처리

     public void signUp() {
    	 
         String id=suv.getJtfId().getText().trim();
         String pass1 = new String(suv.getJpfPass1().getPassword());
         String pass2 = new String(suv.getJpfPass2().getPassword());
         String name = suv.getJtfName().getText().trim();
         String ssn1 = suv.getJtfSsn1().getText().trim();
         String ssn2 = new String(suv.getJpfSsn2().getPassword());
         String tel = suv.getJtfTel().getText().trim();
         String email = suv.getJtfEmail().getText().trim();
         String addr = suv.getJtfAddr1().getText().trim();
         String detailAddr = suv.getJtfAddr2().getText().trim();
         String answer = suv.getJtfAnswer().getText().trim();
         String userType ="";//userType 받아오기
         String questionType="";//qestionType 받기
         
         
         
              if(id.isEmpty()) {JOptionPane.showMessageDialog(suv, "아이디를 입력해주세요");
                   return;
              };
              if(id.contains(" ")) {JOptionPane.showMessageDialog(suv, "아이디에 공백을 입력할 수 없습니다."); return;};
              if(id.length()>15) {
                   JOptionPane.showMessageDialog(suv, "아이디는 최대 15자까지 가능합니다." );
                   suv.getJtfId().requestFocus();
                   return;
              };//if id 15자 검증
              if( pass1==null || pass1.equals("")){
                   JOptionPane.showMessageDialog(suv, "비밀번호를 입력하세요.");
                   suv.getJpfPass1().requestFocus();
                   return;
              };//pass1 입력 받기
              if( pass2==null || pass2.equals("")){
                   JOptionPane.showMessageDialog(suv, "비밀번호를 확인해주세요.");
                   suv.getJpfPass2().requestFocus();
                   return;
              };//pass2 입력 받기
              if(name==null || name.equals("")) {
                   JOptionPane.showMessageDialog(suv, "이름을 입력해주세요");
                   suv.getJtfName().requestFocus();
                   return;
              };//이름 받기
              if(ssn1==null || ssn1.equals("")) {JOptionPane.showMessageDialog(suv, "주민번호를 입력해주세요");
                   suv.getJtfSsn1().requestFocus();return;};
              if(ssn2==null || ssn2.equals("")) {JOptionPane.showMessageDialog(suv, "주민번호를 입력해주세요");
                   suv.getJpfSsn2().requestFocus();return;};//주민번호 입력받기
              if(tel==null||tel.equals("")) {JOptionPane.showMessageDialog(suv, "연락처를 입력해주세요");
                   suv.getJtfTel().requestFocus(); return;};//연락처 받기
              if(email==null||email.equals("")) {JOptionPane.showMessageDialog(suv, "이메일을 입력해주세요");
                   suv.getJtfEmail().requestFocus(); return;};//이메일 받기
   /*test    */     if(addr==null||addr.equals("")) {JOptionPane.showMessageDialog(suv, "주소를 입력해주세요");
   /* test        */     suv.getJtfAddr1().requestFocus(); return;};//주소 받기
              if(detailAddr==null||detailAddr.equals("")) {JOptionPane.showMessageDialog(suv, "상세주소를 입력해주세요");
                   suv.getJtfAddr2().requestFocus(); return;};//상세주소 받기
              if(answer==null||answer.equals("")) {JOptionPane.showMessageDialog(suv, "질문의 답을 입력해주세요");
                   suv.getJtfAnswer().requestFocus(); return;};//질문의 답
              ///////////////////////
                   
              /*if( !(pass1.matches(".*[A-Z].*")) || !(pass1.matches(".*[a-z].*")) || !(pass1.matches(".*\\d.*")) || !(pass1.matches(".*[~!.......].*")) ) {
           	   JOptionPane.showMessageDialog(suv, "비밀번호에 대문자, 소문자, 특수문자가 들어가야합니다.");
           	   return;
              };*/
           	   
              if(!pass2.equals(pass1)) {JOptionPane.showMessageDialog(suv, "비밀번호가 일치하지 않습니다");
              /*     System.out.println(pass1+"/"+pass2);
                   System.out.println(!pass2.equals(pass1));
                   System.out.println(pass2!=pass1);*/
                   suv.getJpfPass1().setText("");
                   suv.getJpfPass2().setText("");
                   suv.getJpfPass1().requestFocus();
                   return;
              };//비밀번호 일치확인
              
              /////비밀번호 특수문자/ 대문자 / 소문자 조합////////////////약간 남아있음
              char[] lowerCase = { 
      				'a','b','c','d','e','f','g',
      				'h','i','j','k','l','m','n','o','p','q','r',
      				's','t','u','v','w','x','y','z'};
      		
      		char[] upperCase = {
      				'A','B','C','D','E','F','G',
      				'H','I','J','K','L','M','N','O','P','Q','R',
      				'S','T','U','V','W','X','Y','Z'};
      		
      		char[] spSymbol = {'!','@','#','$','%','^','&','*','(',')','-','_','+','='};
            
      		boolean lowerFlag = false;
      		boolean upperFlag =false;
      		boolean spcFlag = false;
      		
      		if( (pass1.length()>13)) {
      			JOptionPane.showMessageDialog(suv, "비밀번호는 최대 12자리 까지 가능합니다.");
      			suv.getJpfPass1().setText("");
               suv.getJpfPass2().setText("");
               suv.getJpfPass1().requestFocus();
               return;
      		}else{
      			for(int i=0; i<pass1.length(); i++) {
   				for(int j=0; j<lowerCase.length; j++) {
   					if(pass1.charAt(i) == lowerCase[j]) {
   						lowerFlag = true;
   					}
   				}
   				for(int j=0; j<upperCase.length; j++) {
   					if(pass1.charAt(i) == upperCase[j]) {
   						upperFlag = true;
   					}
   				}
   				for(int j=0; j<spSymbol.length; j++) {
   					if(pass1.charAt(i) == spSymbol[j]) {
   						spcFlag = true;
   					}
   				}
   			}//end for
      			
      			if(lowerFlag && upperFlag && spcFlag) {
      				//System.out.println("올바른 비밀번호!!");
      			}else {
      				JOptionPane.showMessageDialog(suv, "비밀번호엔 소문자,대문자,특수문자가 포함되어야합니다.");
      				suv.getJpfPass1().setText("");
      				suv.getJpfPass2().setText("");
      				suv.getJpfPass1().requestFocus();
      				return;
      			}
   			
      		}//end else
      		
              /////////////////////////////////////////////주민번호 검증 완료 
              String ssn = ssn1+ssn2;
              int[] flagNum= {2,3,4,5,6,7,8,9,2,3,4,5};
              int sum = 0;
              
             for(int i=0; i<flagNum.length; i++) {
           	  
           	  sum += ((int)ssn.charAt(i)-48)*((flagNum[i]));
           	  //System.out.println("ssn.charat :"+ssn.charAt(i));
           	 // System.out.println("flag num :"+flagNum[i]);
           	  //System.out.println("sum :"+sum);
           	  ///문제 생김 - 왜 sum 이 제대로 안나오지 데이터형 때문에 그런가?
           	  }//end for
             sum = (11-(sum%11))%10;
              if(!(sum == ssn.charAt(12)-48 )) {
           	   JOptionPane.showMessageDialog(suv, "올바른 주민번호가 아닙니다.");
           	   return;
              }//end if       //////////주민번호 검증 완료
              ssn = ssn1+"-"+ssn2;
        
              
              String chkTel = tel.replaceAll("-", "");
              try {
            	  Integer.parseInt(chkTel);
              }catch (NumberFormatException npe) {
            	  JOptionPane.showMessageDialog(suv, "연락처는 숫자만 입력 가능합니다.\n 형식)000-0000-0000");
            	  return;
              }
              if(chkTel.length()<10) {
            	  JOptionPane.showMessageDialog(suv, "전화번호 숫자 안맞음 전화번호 형식이 잘못되었습니다.\n"+" 형식)000-0000-0000");
            	  return;
              }//end else
              // 000-0000-0000
              // 0123456789123
              if(tel.indexOf("-")!=3 || !tel.substring((tel.length())-5, tel.length()-4).equals("-")) {
            	  System.out.println("첫쨰 하이픈 위치 :"+tel.indexOf("-") +"//둘째 하이픈 인덱스 가져오기/"+tel.substring((tel.length())-5, tel.length()-4)+"tel의 길이 : "+tel.length());
            	  JOptionPane.showMessageDialog(suv, "연락처 형식이 잘못되었습니다.하이픈 - ,- 인덱스로 거르기");
            	  return;
              };
            	 
              int cnt=0;
              for(int i=0; i<tel.length(); i++ ) {
            	  if(Character.toString(tel.charAt(i)).equals("-")){
            			  cnt++;
            		  };//end if
            	  }//end for
            	  if(cnt!=2) {
            		  JOptionPane.showMessageDialog(suv, "하이픈2개이상 전화번호 형식이 잘못되었습니다.\n"+" 형식)000-0000-0000");
            		  return;
            	  };//하이픈 2개인지 검사 끝
              	
              //////////////////////////연락처 양식 검증////////////여기까지
	              
              ///////////아래부터 이메일 검증//////////
              if(email.length() < 14) {
           	  JOptionPane.showMessageDialog(suv, "이메일은 최소 14자 이상이어야합니다.");
           	  	return;
              }else {
           	   if(email.indexOf("@")==-1 || email.indexOf(".")==-1) {
           		   JOptionPane.showMessageDialog(suv, "이메일 형식이 잘못되었습니다.");
           		   return;
           	   }//end if
              }//end else
              
             
              ////////////////////////이메일 검증 끝 ////////////////
      /////유저 타입 받아오기 
              if(suv.getJrbEe().isSelected()){
            	  userType="E";
              }else if(suv.getJrbEr().isSelected()){
            	  userType="R";
              }//end if /////userType 받아오기
              
              //////////퀘스쳔 타입
              if(suv.getJcbQuestion().getSelectedIndex()==0) {
            	  questionType = "0";
              }else if(suv.getJcbQuestion().getSelectedIndex()==1){
            	  questionType ="1";
              }//QT 받기 끝    
    	 
    	 String resultMsg = "";
          System.out.println("검증완료");
          UserInsertVO uivo = new UserInsertVO( id, pass1,name,ssn,tel,email, addrSeq, detailAddr, questionType ,answer,userType);
          try {
			resultMsg=c_dao.insertUser(uivo);
			JOptionPane.showMessageDialog(suv, resultMsg );
			suv.dispose();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(suv, "회원가입 실패");
			System.out.println("DB연동관련 insert 오류");
		}
          
     }//signUp

	public String getAddrSeq() {
		return addrSeq;
	}

	public void setAddrSeq(String addrSeq) {
		this.addrSeq = addrSeq;
	}

	public SignUpView getSuv() {
		return suv;
	}
}//class