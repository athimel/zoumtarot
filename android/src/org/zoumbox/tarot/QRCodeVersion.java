package org.zoumbox.tarot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class QRCodeVersion extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_code);

        // Instantiate an ImageView and define its properties
        ImageView i = (ImageView)findViewById(R.id.qr_image);
        i.setImageResource(R.drawable.qrcode_1_1);

    }

}
