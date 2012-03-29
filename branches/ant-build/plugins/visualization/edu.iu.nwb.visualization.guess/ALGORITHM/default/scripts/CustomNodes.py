from java.awt.image import BufferedImage
from java.awt.image import ConvolveOp
from java.awt.image import Kernel
from java.awt import Graphics2D
from java.awt import RenderingHints
from java.awt import Color
from java.awt import Paint
from java.awt import GradientPaint
from java.awt import AlphaComposite
from java.awt.geom import RoundRectangle2D
from jarray import array
from java.awt import Toolkit

def aquaNode(text,fSize):
	vWidth = 300
	vHeight = 100

	BLUR = array([0.10, 0.10, 0.10, 0.10, 0.30, 0.10, 0.10, 0.10, 0.10],'f')
	inset = 10
	buttonColor = Color.BLUE.brighter().brighter().brighter()
	foregroundColor = Color(1,1,1,0.6)

	toRet = BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB)
	g2 = toRet.createGraphics()

	testFont = g2.getFont().deriveFont(Font.BOLD,fSize)
	fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(testFont); 
	vWidth = Math.max(100,Math.round(fontMetrics.stringWidth(text) * 1.5)) 
	vHeight = Math.max(30,Math.round((fontMetrics.getHeight() + fontMetrics.getAscent())*1.5))
	
	toRet = BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB)
	g2 = toRet.createGraphics()
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

	vBuffer = BufferedImage(vWidth, vHeight, BufferedImage.TYPE_INT_RGB)
	bg = vBuffer.createGraphics()
	bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

	vButtonHeight = vHeight - (inset * 2);
	vButtonWidth = vWidth - (inset * 2);
	vArcSize = vButtonHeight;

	bg.setColor(getDisplayBackground());
	bg.fillRect(0, 0, vWidth, vHeight);

	vGradientStartColor =  buttonColor.darker().darker().darker();
	vGradientEndColor = buttonColor.brighter().brighter().brighter();
	vPaint = GradientPaint(0, inset, vGradientStartColor, 0, vButtonHeight, vGradientEndColor, false);
	bg.setPaint(vPaint);

	bg.fillRoundRect(inset, inset, vButtonWidth, vButtonHeight, vArcSize, vArcSize);
	vHighlightInset = 2;
	vButtonHighlightHeight = vButtonHeight - (vHighlightInset * 2);
	vButtonHighlightWidth = vButtonWidth - (vHighlightInset * 2);
	vHighlightArcSize = vButtonHighlightHeight;

	vGradientStartColor = Color.WHITE;
	vGradientEndColor = buttonColor.brighter();
	vPaint = GradientPaint(0,inset+vHighlightInset,vGradientStartColor,0,inset+vHighlightInset+(vButtonHighlightHeight/2), buttonColor.brighter(), false);

	bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.8));
	bg.setPaint(vPaint);
	bg.setClip(RoundRectangle2D.Float(inset+vHighlightInset,inset+vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight / 2,vButtonHighlightHeight / 3,vButtonHighlightHeight /3));
	bg.fillRoundRect(inset+vHighlightInset,inset+vHighlightInset,vButtonHighlightWidth,vButtonHighlightHeight,vHighlightArcSize,vHighlightArcSize);

	vBlurOp = ConvolveOp(Kernel(3, 3, BLUR));
	vBlurredBase = vBlurOp.filter(vBuffer, None);

	g2.drawImage(vBlurredBase, 0, 0, None);

	g2.setColor(foregroundColor);
	
	g2.setFont(testFont);
	vMetrics = g2.getFontMetrics();
	vStringBounds = vMetrics.getStringBounds(text,g2);

	xt = ((vWidth / 2) - (vStringBounds.getWidth() / 2));
	yt = ((vHeight / 2) + (vStringBounds.getHeight() / 2)) - vMetrics.getDescent();

	g2.drawString(text,xt,yt);

	toRet = toRet.getSubimage(inset,inset,vButtonHighlightWidth+inset/2,vButtonHighlightHeight+inset/2);
	return toRet

def convertAllToAqua():
	for n in g.nodes:
		n.style = 7
		n.width = -1
		n.height = -1
		n.image = aquaNode(n.label,12)
