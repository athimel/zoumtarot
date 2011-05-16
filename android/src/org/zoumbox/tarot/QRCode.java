package org.zoumbox.tarot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class QRCode extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_code);

        // Instantiate an ImageView and define its properties
        ImageView i = (ImageView)findViewById(R.id.qr_image);
        i.setImageResource(R.drawable.qrcode_market);

    }

}
