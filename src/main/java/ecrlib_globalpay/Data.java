package ecrlib_globalpay;

public class Data {
	//Transaction Data object clss
		public String InvoiceNumber;
		public String BatchNumver;
		public String TerminalID;
		public String MerchanID;
		public String HostName;
		public String ApproveCode;
		public String ResponeseCode;
		public String ReferanceNumber;
		public String CardNumberBin;
		public String CardNumberLast;
		public String CardType;
		public String Amount;
		public String CardName;
		public String ErrorCode;
		public int inTimeOut;

		public Data(
				String InvoiceNumber,
				 String BatchNumver,
				 String TerminalID,
				 String MerchanID,
				 String HostName,
				 String ApproveCode,
				 String ResponeseCode,
				 String ReferanceNumber,
				 String CardNumberBin,
				 String CardNumberLast,
				 String CardType,
				 String Amount,
				 String CardName,
				 String ErrorCode,
				int inTimeOut){


		this.InvoiceNumber =InvoiceNumber ;
		this.BatchNumver=BatchNumver;
		this.TerminalID=TerminalID;
		this.MerchanID=MerchanID;
		this.HostName=HostName;
		this.ApproveCode=ApproveCode;
		this.ResponeseCode=ResponeseCode;
		this.ReferanceNumber=ReferanceNumber;
		this.CardNumberBin=CardNumberBin;
		this.CardNumberLast=CardNumberLast;
		this.CardType=CardType;
		this.Amount=Amount;
		this.CardName=CardName;
	    this.ErrorCode=ErrorCode;
		this.inTimeOut=inTimeOut;
		}
		
	}