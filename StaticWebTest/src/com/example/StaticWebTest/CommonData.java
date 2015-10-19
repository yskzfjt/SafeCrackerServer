package com.example.StaticWebTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

// import javax.jdo.annotations.IdGeneratorStrategy;
// import javax.jdo.annotations.IdentityType;
// import javax.jdo.annotations.PersistenceCapable;
// import javax.jdo.annotations.Persistent;
// import javax.jdo.annotations.PrimaryKey;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

 
public class CommonData {
    public enum PHASE { 
	INIT,			//initialize.
	    LOGIN,		//connect to server and new game starts automatically. TRANSACTION
	    NEW_GAME,		//clear digits and history... TRANSACTION
	    SELECT,		//selecting bet and 4 digits and push buttos.
	    TRY,		//exec button pushed. waiting for server response. TRANSACTION
	    GIVE_UP,		//to new game starts.
	    RESULT,		//result for each try. 
	    FINALE,		//result for whole game. consumed maxTries times.
	    WIN,		//WIN.
	    ERROR,		//showing error dialog.
	    LOGOUT,		//disconnect from server. closing page or quitting web browser or whatever.
	    MAX };

    ////////////////////////////////////////////////////////////////////
    //version for CommonData.
    ////////////////////////////////////////////////////////////////////
    public String version;

    ////////////////////////////////////////////////////////////////////
    //User Info
    ////////////////////////////////////////////////////////////////////
    //user ID.optional.
    public String userID;

    //seq and token. 
    public String seq;
    public String token;

    //creditBalance
    public int creditBalance;

    //timestamp
    public String timeStamp;


    ////////////////////////////////////////////////////////////////////
    //Game Info
    ////////////////////////////////////////////////////////////////////
    public PHASE phase = PHASE.INIT;
    public PHASE oldPhase = PHASE.INIT;

    //game no since login
    public int gameCount = -1;

    //try no since new game
    public int tryCount = 0;

    //input numbers
    public int[] digits = null;
    //cracked numbers
    public int[] crackedDigits = null;

    //bet
    public int bet;

    //reward
    public int reward;

    //auto or manual
    public boolean isAuto = false;

    private int maxTries = 7;
    private int maxDigits = 4;

    private void NewDigits(){
	digits = new int[maxTries * maxDigits];
	crackedDigits = new int[ maxDigits ];
    }
    public CommonData(){
	NewDigits();
    }

    String ParseString( Element r, String tag ){
	NodeList nl=r.getElementsByTagName(tag);
	return nl.item(0).getTextContent();
    }
    int ParseInt( Element r, String tag ){
	NodeList nl=r.getElementsByTagName(tag);
	return Integer.parseInt( nl.item(0).getTextContent() );
    }
    boolean ParseBool( Element r, String tag ){
	NodeList nl=r.getElementsByTagName(tag);
	return Boolean.parseBoolean( nl.item(0).getTextContent() );
    }
    PHASE ParsePHASE( Element r, String tag ){
	NodeList nl=r.getElementsByTagName(tag);
	return CommonData.PHASE.valueOf( nl.item(0).getTextContent() );
    }

    
    public boolean Parse( String xmlString ) throws IOException, XPathExpressionException{
 	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 	DocumentBuilder documentBuilder;
 	try {
	    documentBuilder = factory.newDocumentBuilder();
	    Document document;
	    document = documentBuilder.parse( new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))) );
	    Element root = document.getDocumentElement();

	    version = ParseString( root, "version" );
	    userID = ParseString( root, "userID" );
	    seq = ParseString( root, "seq" );
	    token = ParseString( root, "token" );
	    creditBalance = ParseInt( root, "creditBalance" );
	    timeStamp = ParseString( root, "timeStamp" );
	    phase = ParsePHASE( root, "phase" );
	    oldPhase = ParsePHASE( root, "oldPhase" );
	    gameCount = ParseInt( root, "gameCount" );
	    tryCount = ParseInt( root, "tryCount" );
	    bet = ParseInt( root, "bet" );
	    reward = ParseInt( root, "reward" );
	    isAuto = ParseBool( root, "isAuto" );

	    XPathFactory xPathFactory = XPathFactory.newInstance();
	    XPath xPath = xPathFactory.newXPath();

	    NodeList digits = (NodeList)xPath.evaluate("/commonData/digits/int",document, XPathConstants.NODESET);
	    for (int i=0; i < digits.getLength(); ++i) {
		this.digits[i] = Integer.parseInt( digits.item(i).getTextContent() );
	    }

	    NodeList crackedDigits = (NodeList)xPath.evaluate("/commonData/crackedDigits/int",document, XPathConstants.NODESET);
	    for (int i=0; i < crackedDigits.getLength(); ++i) {
		this.crackedDigits[i] = Integer.parseInt( crackedDigits.item(i).getTextContent() );
	    }
			

 	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
 	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return false;
 	}

	return true;
    }

    public int Index( int i, int j ){ 
	return i*maxDigits + j;
    }
    public boolean IsWin(  ){
	return crackedDigits[ 0 ] >= 0 &&
	    crackedDigits[ 1 ] >= 0 &&
	    crackedDigits[ 2 ] >= 0 &&
	    crackedDigits[ 3 ] >= 0;
    }
    public void CrackDigitAt( int c ){
	crackedDigits[ c ] = GetDigitAt( c );
    }
    public int GetDigitAt( int c ){
	return GetDigitAt( tryCount, c );
    }
    public int GetDigitAt( int t, int c ){
	return digits[ Index(t, c) ];
    }
    public int GetCrackedDigitAt( int c ){
	return crackedDigits[ c ];
    }
    public boolean IsUniqueDigitAt( int c ){
	if( tryCount == 0 ) return true;

	int cur = GetDigitAt( c );
	for( int i=0; i<tryCount; ++i ){
	    if( GetDigitAt( i, c ) == cur ) return false;
	}
	return true;
    }

    void DoTransaction(){
	Random rnd = new Random();
	switch( phase ){
	case LOGIN:
	    System.out.println("LOGIN TRANSACTION" );
	    token = "@todo: token must calculated from " + "seq";
	    creditBalance = rnd.nextInt(5000) + 2500;
	    break;
	case NEW_GAME:
	    System.out.println("NEW GAME TRANSACTION" );
	    break;
	case TRY:
	    System.out.println("TRY TRANSACTION" );
	    creditBalance -= bet;
	    for( int i=0; i<maxDigits; ++i ){
		if( GetCrackedDigitAt(i) < 0 &&
		    IsUniqueDigitAt(i) && 
		    rnd.nextInt( 1000 ) < 200 ){
		    CrackDigitAt(i);
		}
	    }
	    if( IsWin() ){
		int r = (maxTries - tryCount);
		reward += bet * r;
	    }
	    break;
	default:
	    break;
	}
    }


}
