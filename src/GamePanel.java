import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel {
    private ArrayList<Block> blocks = new ArrayList<>();
    private int score = 0;
    private boolean gameActive = false;
    private int timeLeft = 30;
    private Random rand = new Random();
    private Timer timer;
    private JButton startButton = new JButton("Start Game");

    public GamePanel() {
        setLayout(null);
        setFocusable(true);

        startButton.setBounds(350, 250, 100, 50);
        add(startButton);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                } else {
                    gameActive = false;
                    timer.stop();
                    startButton.setVisible(true);
                }
                repaint();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameActive) {
                    gameActive = true;
                    score = 0;
                    timeLeft = 30;
                    blocks.clear();
                    startButton.setVisible(false);

                    timer.start();

                    generateBlock();

                    repaint();
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameActive) return;

                Iterator<Block> iterator = blocks.iterator();
                while (iterator.hasNext()) {
                    Block block = iterator.next();
                    if (block.rect.contains(e.getPoint())) {
                        iterator.remove();
                        score++;
                        generateBlock();
                        break;
                    }
                }
                repaint();
            }
        });


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Rezultāts: " + score, 10, 20);
        g.drawString("Atlikušais laiks: " + timeLeft, 10, 40);

        if (!gameActive && timeLeft == 0) {
            g.drawString("Laiks beidzies! Rezultāts: " + score, getWidth() / 2 - 100, getHeight() / 2);
        }

        for (Block block : blocks) {
            g.setColor(block.color);
            g.fillRect(block.rect.x, block.rect.y, block.rect.width, block.rect.height);
        }
    }


    private void generateBlock() {
        if (!gameActive) return;
        int x = rand.nextInt(Math.max(1, this.getWidth() - 50));
        int y = rand.nextInt(Math.max(1, this.getHeight() - 50));
        int width = 20 + rand.nextInt(81);
        int height = 20 + rand.nextInt(81);
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        blocks.add(new Block(new Rectangle(x, y, width, height), color));
    }

    private static class Block {
        Rectangle rect;
        Color color;

        Block(Rectangle rect, Color color) {
            this.rect = rect;
            this.color = color;
        }
    }
}