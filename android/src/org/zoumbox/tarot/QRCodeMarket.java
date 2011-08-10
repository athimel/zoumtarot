/*
 * #%L
 * ZoumTarot :: android
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Zoumbox.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.zoumbox.tarot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * @author Arnaud Thimel <thimel@codelutin.com>
 */
public class QRCodeMarket extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_code);

        // Instantiate an ImageView and define its properties
        ImageView i = (ImageView) findViewById(R.id.qr_image);
        i.setImageResource(R.drawable.qrcode_market);

    }

}
