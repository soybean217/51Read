package com.wanpg.bookread.ui.read.txt;

import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.wanpg.bookread.ui.read.BookBasePaint;
import com.wanpg.bookread.utils.DisplayUtil;
import com.wanpg.bookread.utils.FileUtil;
import com.wanpg.bookread.utils.Util;

public class TxtPaint extends BookBasePaint{
    private MappedByteBuffer m_mbBuf = null;
    private int m_mbBufLen = 0;
    private int m_mbBufBegin = 0;
    public int m_mbBufEnd = 0;
    private String m_strCharsetName = "UTF-8";
    private int mWidth;
    private int mHeight;

    private Bitmap m_backBitmap = null;

    private Vector<String> m_lines = new Vector<String>();

    private int m_fontSize = 27;
    private int m_textColor = Color.BLACK;
    private int m_backColor = Color.BLACK; // 背景颜色
    private int marginWidth = 15; // 左右与边缘的距离
    private int marginHeightDp = 20; // 上下与边缘的距离

    private Typeface typeface = Typeface.DEFAULT;

    private int m_msgFontSize = 17;

    private int mLineCount; // 每页可以显示的行数
    private float mVisibleHeight; // 绘制内容的宽
    private float mVisibleWidth; // 绘制内容的宽
    private boolean m_isfirstPage, m_islastPage;

    public String percent = "0%";

    private int m_lineSpaceing = 3;

    /**
     * 是否显示顶部信息 *
     */
    private boolean isShowHeadMsg;
    /**
     * 是否显示底部信息 *
     */
    private boolean isShowBottomMsg;
    /**
     * 电池是否用百分数显示 *
     */
    private boolean isBatteryPercent;

    private Paint mPaint;
    private Paint mMsgPaint;

    private String title = "ssss";
    private String bookName;

    private Bitmap bmBattery = null;
    private int batteryLevel = 0;
    public TxtPaint(int pageBegin, int pageEnd) {
        // TODO Auto-generated constructor stub
        this.m_mbBufBegin = pageBegin;
        this.m_mbBufEnd = m_mbBufBegin;
    }

    public void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Align.LEFT);
        mPaint.setTextSize(m_fontSize);
        mPaint.setColor(m_textColor);
        mPaint.setTypeface(typeface);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - DisplayUtil.dp2px(marginHeightDp) * 2;
        mLineCount = (int) (mVisibleHeight / (m_fontSize + m_lineSpaceing)); // 可显示的行数

        if (mWidth > 540) {
            m_msgFontSize = 25;
        }
        mMsgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMsgPaint.setAntiAlias(true);
        mMsgPaint.setColor(m_textColor);
        mMsgPaint.setTextSize(m_msgFontSize);
    }

    protected byte[] readParagraphBack(int nFromPos) {
        int nEnd = nFromPos;
        int i;
        byte b0, b1;
        if (m_strCharsetName.equals("UTF-16LE")) {
            i = nEnd - 2;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                b1 = m_mbBuf.get(i + 1);
                if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
                    i += 2;
                    break;
                }
                i--;
            }

        } else if (m_strCharsetName.equals("UTF-16BE")) {
            i = nEnd - 2;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                b1 = m_mbBuf.get(i + 1);
                if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
                    i += 2;
                    break;
                }
                i--;
            }
        } else {
            i = nEnd - 1;
            while (i > 0) {
                b0 = m_mbBuf.get(i);
                if (b0 == 0x0a && i != nEnd - 1) {
                    i++;
                    break;
                }
                i--;
            }
        }
        if (i < 0)
            i = 0;
        int nParaSize = nEnd - i;
        int j;
        byte[] buf = new byte[nParaSize];
        for (j = 0; j < nParaSize; j++) {
            buf[j] = m_mbBuf.get(i + j);
        }
        return buf;
    }


    // 读取上一段落
    protected byte[] readParagraphForward(int nFromPos) {
        int nStart = nFromPos;
        int i = nStart;
        byte b0, b1;
        // 根据编码格式判断换行
        if (m_strCharsetName.equals("UTF-16LE")) {
            while (i < m_mbBufLen - 1) {
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x0a && b1 == 0x00) {
                    break;
                }
            }
        } else if (m_strCharsetName.equals("UTF-16BE")) {
            while (i < m_mbBufLen - 1) {
                b0 = m_mbBuf.get(i++);
                b1 = m_mbBuf.get(i++);
                if (b0 == 0x00 && b1 == 0x0a) {
                    break;
                }
            }
        } else {
            while (i < m_mbBufLen) {
                b0 = m_mbBuf.get(i++);
                if (b0 == 0x0a) {
                    break;
                }
            }
        }
        int nParaSize = i - nStart;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = m_mbBuf.get(nFromPos + i);
        }
        return buf;
    }

    protected Vector<String> pageDown() {
        String strParagraph = "";
        Vector<String> lines = new Vector<String>();
        while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen) {
            byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
            m_mbBufEnd += paraBuf.length;
            try {
                strParagraph = new String(paraBuf, m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String strReturn = "";
            if (strParagraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                strParagraph = strParagraph.replaceAll("\r\n", "");
            } else if (strParagraph.indexOf("\n") != -1) {
                strReturn = "\n";
                strParagraph = strParagraph.replaceAll("\n", "");
            }

            if (strParagraph.length() == 0) {
                lines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
                        null);
                lines.add(strParagraph.substring(0, nSize));
                strParagraph = strParagraph.substring(nSize);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }
            if (strParagraph.length() != 0) {
                try {
                    m_mbBufEnd -= (strParagraph + strReturn)
                            .getBytes(m_strCharsetName).length;
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    protected void pageUp() {
        if (m_mbBufBegin < 0)
            m_mbBufBegin = 0;
        Vector<String> lines = new Vector<String>();
        String strParagraph = "";
        while (lines.size() < mLineCount && m_mbBufBegin > 0) {
            Vector<String> paraLines = new Vector<String>();
            byte[] paraBuf = readParagraphBack(m_mbBufBegin);
            m_mbBufBegin -= paraBuf.length;
            try {
                strParagraph = new String(paraBuf, m_strCharsetName);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "");
            strParagraph = strParagraph.replaceAll("\n", "");

            if (strParagraph.length() == 0) {
                paraLines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
                        null);
                paraLines.add(strParagraph.substring(0, nSize));
                strParagraph = strParagraph.substring(nSize);
            }
            lines.addAll(0, paraLines);
        }
        while (lines.size() > mLineCount) {
            try {
                m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
                lines.remove(0);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        m_mbBufEnd = m_mbBufBegin;
        return;
    }

    public void prePage() {
        if (m_mbBufBegin <= 0) {
            m_mbBufBegin = 0;
            m_isfirstPage = true;
            return;
        } else m_isfirstPage = false;
        m_lines.clear();
        pageUp();
        m_lines = pageDown();
    }

    public void nextPage() {
        if (m_mbBufEnd >= m_mbBufLen) {
            m_islastPage = true;
            return;
        } else m_islastPage = false;
        m_lines.clear();
        m_mbBufBegin = m_mbBufEnd;
        m_lines = pageDown();
    }

    public void curPage() {
        m_lines.clear();
        m_mbBufEnd = m_mbBufBegin;
        m_lines = pageDown();
    }

    public void onDrawPage(Canvas c) {
        float x = 0;
        float y = DisplayUtil.dp2px(marginHeightDp) * 7 / 6 - m_lineSpaceing;
        float lenStr = 0;
        if (m_backBitmap == null)
            c.drawColor(m_backColor);
        else
            c.drawBitmap(m_backBitmap, 0, 0, null);
        //逐行取出str，并画到界面上
        for (String strLine : m_lines) {
            float lenTmp = mPaint.measureText(strLine);
            if (lenTmp > lenStr) {
                lenStr = lenTmp;
            }
        }
        x = (mWidth - lenStr) / 2;
        for (String strLine : m_lines) {

            y += m_fontSize + m_lineSpaceing;
            c.drawText(strLine, x, y, mPaint);
        }
        //画出标题，章节，进度，时间，电量
        //onDrawMsg(c);
        if (isShowHeadMsg) {
            onDrawHeadMsg(c);
        }
        if (isShowBottomMsg) {
            onDrawBottomMsg(c);
        }
    }

    private void onDrawHeadMsg(Canvas canvas) {
        // TODO Auto-generated method stub
        String s = bookName.replace(".txt", "");
        canvas.drawText(s, marginWidth * 3 / 2, DisplayUtil.dp2px(marginHeightDp) * 4 / 5, mMsgPaint);
        //canvas.drawText(title, mWidth-marginWidth*3/2-lenTitle, marginHeight*2/3, mMsgPaint);

    }

    Rect mBatterySrc = new Rect();
    Rect mBatteryDst = new Rect();
    private void onDrawBottomMsg(Canvas canvas) {
        // TODO Auto-generated method stub
        percent = Util.countPercent(m_mbBufEnd, m_mbBufLen);
        String time = Util.getSysTime();
        float lenBattery = 0f;
        float heiBattery = 0f;
        if (isBatteryPercent) {
            lenBattery = mMsgPaint.measureText("电量:" + batteryLevel + "%");
            canvas.drawText("电量:" + batteryLevel + "%", mWidth - marginWidth * 3 / 2 - lenBattery, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);
        } else {
        	if(bmBattery!=null && !bmBattery.isRecycled()){
	            lenBattery = m_msgFontSize * 45 / 30;
	            heiBattery = lenBattery * bmBattery.getHeight() / bmBattery.getWidth();
	            mBatteryDst.left = (int) (mWidth - marginWidth * 3 / 2 - lenBattery);
	            mBatteryDst.top = (int) (mHeight - DisplayUtil.dp2px(marginHeightDp) / 4 - heiBattery + 2);
	            mBatteryDst.right = (int) (mBatteryDst.left + lenBattery);
	            mBatteryDst.bottom = (int) (mBatteryDst.top + heiBattery);
	            
	            mBatterySrc.top = 0;
	            mBatterySrc.left = 0;
	            mBatterySrc.right = bmBattery.getWidth();
	            mBatterySrc.bottom = bmBattery.getHeight();
	            
	            canvas.drawBitmap(bmBattery, mBatterySrc, mBatteryDst, mMsgPaint);
        	}
        }
        float lenTime = mMsgPaint.measureText(time);
        canvas.drawText(percent, marginWidth * 3 / 2, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);
        canvas.drawText(time, mWidth - marginWidth * 3 / 2 - lenTime - lenBattery * 3 / 2, mHeight - DisplayUtil.dp2px(marginHeightDp) / 4, mMsgPaint);
    }


    @Override
    public boolean isFirstPage() {
        return m_isfirstPage;
    }

    @Override
    public boolean isLastPage() {
        return m_islastPage;
    }

    public int getM_mbBufBegin() {
        return m_mbBufBegin;
    }

    public void setM_mbBufBegin(int m_mbBufBegin) {
        this.m_mbBufBegin = m_mbBufBegin;
    }

    public int getM_mbBufEnd() {
        return m_mbBufEnd;
    }

    public void setM_mbBufEnd(int m_mbBufEnd) {
        this.m_mbBufEnd = m_mbBufEnd;
    }

    public MappedByteBuffer getM_mbBuf() {
        return m_mbBuf;
    }

    public void setM_mbBuf(MappedByteBuffer m_mbBuf) {
        this.m_mbBuf = m_mbBuf;
        m_mbBufLen = m_mbBuf.remaining();
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = m_mbBuf.get(i);
        }
        m_strCharsetName = FileUtil.getCharsetType(b);
    }

    public int getM_fontSize() {
        return m_fontSize;
    }

    public void setM_fontSize(int m_fontSize) {
        this.m_fontSize = m_fontSize;
    }

    public int getM_textColor() {
        return m_textColor;
    }

    public void setM_textColor(int m_textColor) {
        this.m_textColor = m_textColor;
    }

    public Bitmap getM_backBitmap() {
        return m_backBitmap;
    }

    public void setM_backBitmap(Bitmap m_backBitmap) {
        this.m_backBitmap = m_backBitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }


    public void setBmBattery(Bitmap bmBattery, int batteryLevel) {
        this.bmBattery = bmBattery;
        this.batteryLevel = batteryLevel;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getMarginWidth() {
        return marginWidth;
    }

    public void setMarginWidth(int marginWidth) {
        this.marginWidth = marginWidth;
    }

    public int getM_lineSpaceing() {
        return m_lineSpaceing;
    }

    public void setM_lineSpaceing(int m_lineSpaceing) {
        this.m_lineSpaceing = m_lineSpaceing;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public boolean isShowHeadMsg() {
        return isShowHeadMsg;
    }

    public void setShowHeadMsg(boolean isShowHeadMsg) {
        this.isShowHeadMsg = isShowHeadMsg;
    }

    public boolean isShowBottomMsg() {
        return isShowBottomMsg;
    }

    public void setShowBottomMsg(boolean isShowBottomMsg) {
        this.isShowBottomMsg = isShowBottomMsg;
    }

    public boolean isBatteryPercent() {
        return isBatteryPercent;
    }

    public void setBatteryPercent(boolean isBatteryPercent) {
        this.isBatteryPercent = isBatteryPercent;
    }

    public Vector<String> getM_lines() {
        return m_lines;
    }

    public void setM_lines(Vector<String> m_lines) {
        this.m_lines = m_lines;
    }

}
