package com.example.jhon.werbservice.Net;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.jhon.werbservice.models.Ciudades;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by jhon on 16/11/16.
 */

public class SyncCiudadesNet  {
    String url;
    URL urlQuery;
    Ciudades ciudades;
    AsyncTask<Void,Void,Void> asyncTask;

    ArrayList<String> headlines = new ArrayList();
    ArrayList<String> links = new ArrayList();
    List<Ciudades> data;

    public static final int SYNC_CORRECT = 0;
    public static final int SYNC_FAILED = 1;

    OnSyncListener onSyncListener;

    public interface OnSyncListener{
        void OnPrepareConection(int state);
        void OnCompleteConection(int state, List<Ciudades> data, String e);
    }

    public SyncCiudadesNet(Ciudades ciudades, String url, OnSyncListener onSyncListener) {
        this.ciudades = ciudades;
        this.url = url;
        this.onSyncListener = onSyncListener;
    }

    public void ConectionWithServer() {
        asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    urlQuery = new URL(url);
                    XmlPullParserFactory factory = XmlPullParserFactory.
                            newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser pullParser = factory.newPullParser();
                    pullParser.setInput(getInputStream(urlQuery), "UTF_8");

                    int eventType = pullParser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (pullParser.getText() != null) {
                            Document doc = null;
                            doc = DocumentBuilderFactory.newInstance().
                                    newDocumentBuilder().parse(
                                    new InputSource(
                                            new StringReader(pullParser.getText())
                                    )
                            );
                            NodeList errNodes = doc.getElementsByTagName("City");
                            NodeList errNodes2 = doc.getElementsByTagName("Country");
                            if (errNodes.getLength() > 0 && errNodes2.getLength() > 0) {
                                for (int i = 0; i < errNodes2.getLength(); i++) {
                                    Ciudades ciudad = new Ciudades();
                                    Element err = (Element) errNodes2.item(i);
                                    ciudad.setPais(err.getChildNodes().item(0).getTextContent());

                                    Element err2 = (Element) errNodes.item(i);
                                    ciudad.setCiudad(err2.getChildNodes().item(0).getTextContent());
                                    data.add(ciudad);
                                }
                            }
                        }
                        eventType = pullParser.next();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onSyncListener.OnCompleteConection(SYNC_CORRECT, data, null);
            }
        }.execute();
    }


    public InputStream getInputStream(URL url2) {
        try {
            return url2.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }


    }
}
