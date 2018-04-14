package cn.unitid.spark.cm.sdk.business;

import cn.com.syan.jcee.cm.exception.JCEECMException;
import cn.com.syan.jcee.common.sdk.key.PKCS5PBES2;
import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.jcee.utils.StringConverter;
import org.spongycastle.asn1.ASN1InputStream;
import org.spongycastle.asn1.ASN1OctetString;
import org.spongycastle.asn1.ASN1Sequence;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.cert.X509CertificateHolder;
import org.spongycastle.operator.InputDecryptorProvider;
import org.spongycastle.operator.bc.BcDefaultDigestProvider;
import org.spongycastle.pkcs.*;
import org.spongycastle.pkcs.bc.BcPKCS12MacCalculatorBuilderProvider;
import org.spongycastle.pkcs.bc.BcPKCS12PBEInputDecryptorProviderBuilder;
import org.spongycastle.util.encoders.Base64;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Created by lenovo on 2017/3/25.
 */
public class PKCS12Service {
    private byte[] privateKey;
    private X509Certificate x509Certificate;


    public PKCS12Service(byte[] data,char[] password) throws PKCSException, IOException, CertificateException {
        Pfx pfx= Pfx.getInstance(data);
        PKCS12PfxPdu pdu=new PKCS12PfxPdu(pfx);
        BcPKCS12MacCalculatorBuilderProvider macCalcProviderBuilder=new BcPKCS12MacCalculatorBuilderProvider(BcDefaultDigestProvider.INSTANCE);
        boolean pw=pdu.isMacValid(macCalcProviderBuilder,password);

        ContentInfo[] contentInfos=pdu.getContentInfos();
        //
        BcPKCS12PBEInputDecryptorProviderBuilder bcdecryptor=new BcPKCS12PBEInputDecryptorProviderBuilder();
        InputDecryptorProvider idp=bcdecryptor.build(password);

       PKCS5PBES2 pkcs5PBES2 = new PKCS5PBES2();
        for(int i=0;i<contentInfos.length;i++){
            if (contentInfos[i].getContentType().equals(PKCSObjectIdentifiers.encryptedData)){
                x509Certificate=parserCertificte(contentInfos[i], idp);
            }
            if (contentInfos[i].getContentType().equals(PKCSObjectIdentifiers.data)){
                privateKey=parserPriKey(contentInfos[i], idp);
            }
        }
    }

    private byte[] parserPriKey(ContentInfo contentInfo,InputDecryptorProvider idp) throws PKCSException, IOException {
        ASN1InputStream aIn = new ASN1InputStream(((ASN1OctetString)contentInfo.getContent()).getOctets());
        SafeBag sb = SafeBag.getInstance((((ASN1Sequence)aIn.readObject()).getObjectAt(0)));
        EncryptedPrivateKeyInfo encInfo = EncryptedPrivateKeyInfo.getInstance(sb.getBagValue());
        PKCS8EncryptedPrivateKeyInfo p8prikey=new PKCS8EncryptedPrivateKeyInfo(encInfo);
        PrivateKeyInfo pkinfo=p8prikey.decryptPrivateKeyInfo(idp);
        return pkinfo.getEncoded();
    }

    private X509Certificate parserCertificte(ContentInfo contentInfo, InputDecryptorProvider idp) throws PKCSException, IOException, CertificateException {
        PKCS12SafeBagFactory factory=new PKCS12SafeBagFactory(contentInfo,idp);
        PKCS12SafeBag[] safeBags=factory.getSafeBags();
        for(int i=0;i<safeBags.length;i++){
            if (safeBags[i].getType().equals(PKCSObjectIdentifiers.certBag))
            {
                X509CertificateHolder x509cert=(X509CertificateHolder)	safeBags[i].getBagValue();
                return   CertificateConverter.fromBinary(x509cert.getEncoded());

            }
        }
        return null;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }
}
