import java.io.*;
import java.net.*;

class Main{
    
    public static void main(String[] args) throws Exception{
        
        importDialogues("?");
        
    }
    
    private static long importDialogues(String at) throws Exception{
    
        try(OutputStream os=new FileOutputStream(at+".txt")){
        
            String[] u=new String[0],m=new String[0];
            for(int od=0;(u=getDialogs(od,200,at)).length>0;od+=200)
                for(int ui=0;ui<u.length;ui++){
                
                    System.out.println("["+(od+ui)+"] vk.com/id"+u[ui]);
                    os.write(("  vk.com/id"+u[ui]+":\n\n").getBytes());
                    os.flush();
                
                    StringBuilder lo=new StringBuilder(""),ms=new StringBuilder("");
                    for(int om=0;(m=getHistory(u[ui],om,200,at,lo,ms)).length>0;om+=200)
                        for(int mi=0;mi<m.length;mi++)
                            if(m[mi].length()>0){
                                os.write(("    "+m[mi]+"\n").getBytes());
                                os.flush();
                            }
                
                    if(ms.length()>0){
                        os.write((" "+ms.toString()+"\n").getBytes());
                        os.flush();
                    }
                        
                    os.write("\n".getBytes());
                    os.flush();
                
                }
         
            return 1;
         
        }
        
    }
    
    private static String[] getDialogs(int o,int c, String at) throws Exception{
        
        try(InputStream is=new URL("https://api.vk.com/method/messages.getDialogs?offset="+o+"&count="+c+"&access_token="+at+"&v=5.73").openStream()){
            
            int bl=0,bp=0;
            byte[] bi=new byte[2048];
            while((bl=is.read(bi,bp,bi.length-bp))!=-1)
                if((bp+=bl)==bi.length)
                    bi=createArrayBytes(bi,2048);
            
            int p=0;
            String si=encodeChars(new String(bi,0,bp)),ua[]=new String[0];
            while((p=si.indexOf("\"id\":",p))>-1)
                if(si.substring((p=si.indexOf(",",p+5))+1,p+8).equals("\"date\":"))
                    ua=addToArrayString(ua,si.substring(p=si.indexOf("\"user_id\":",p)+10,si.indexOf(",",p)));
            
            return ua;
            
        }finally{
            
            Thread.sleep(300);
            
        }
        
    }
    
    private static String[] getHistory(String uid, int o, int c, String at, StringBuilder lo, StringBuilder m) throws Exception{
        
        try(InputStream is=new URL("https://api.vk.com/method/messages.getHistory?offset="+o+"&count="+c+"&user_id="+uid+"&rev=1&access_token="+at+"&v=5.73").openStream()){
            
            int bl=0,bp=0;
            byte[] bi=new byte[2048];
            while((bl=is.read(bi,bp,bi.length-bp))!=-1)
                if((bp+=bl)==bi.length)
                    bi=createArrayBytes(bi,2048);
            
            int p=0;
            String si=encodeChars(new String(bi,0,bp)),ma[]=new String[0];
            while((p=si.indexOf("\"id\":",p))>-1)
                if(si.substring(p=si.indexOf(",",p+5)+1,p+7).equals("\"body\":")){
                    String bo=decodeChars(si.substring(p=si.indexOf("\"body\":",p)+8,si.indexOf("\"",p))),
                           ou=si.substring(p=si.indexOf("\"out\":",p)+6,p+1);
                    if(ou.equals(lo.toString()))
                        m.append(" "+bo);
                    else{
                        if(lo.length()>0)ma=addToArrayString(ma,m.toString());
                        m.delete(0,m.length()).append((ou.equals("0")?"О: ":"Я: ")+bo);
                        lo.delete(0,lo.length()).append(ou);
                    }
                }
            
            return ma;
            
        }finally{
            
            Thread.sleep(300);
            
        }
        
    }
    
    static String encodeChars(String scs){
        
        return scs.replace("\\\\","\u0000").replace("\\\"","\u0001").replace("\\/","\u0002").replace("\\n","\u0003");
        
    }
    
    static String decodeChars(String scs){
        
        return scs.replace("\u0000","\\").replace("\u0001","\"").replace("\u0002","/").replace("\u0003","\n");
        
    }
    
    static byte[] createArrayBytes(byte[] a, int e){
        
        byte[] b=new byte[a.length+e];
        for(int i=0;i<a.length;i++)
            b[i]=a[i];
        
        return b;
        
    }
    
    static String[] addToArrayString(String[] a, String e){
        
        String[] b=new String[a.length+1];
        for(int i=0;i<a.length;i++)
            b[i]=a[i];
        b[a.length]=e;
        
        return b;
        
    }
    
}