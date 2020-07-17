import com.github.kiulian.downloader.OnYoutubeDownloadListener;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.VideoDetails;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.AudioFormat;
import com.github.kiulian.downloader.model.formats.AudioVideoFormat;
import com.github.kiulian.downloader.model.formats.Format;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

public class VideoDownloader {
    JFrame frame;
    String videoId = null;
    JLabel downInfo;

    public VideoDownloader() {
        frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setTitle("Youtube Video/Audio Downloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
    }

    public void download() {

        YoutubeDownloader downloader = new YoutubeDownloader();
        int frameHeight = System.getProperty("os.name").equals("Linux") ? 300 : 320;
        frame.setSize(600, frameHeight);
        JLabel infoLabel = new JLabel();
        infoLabel.setBounds(10, 150, 200, 20);
        JLabel urlLabel = new JLabel("URL video:");
        urlLabel.setBounds(10, 20, 100, 20);

        JLabel osname = new JLabel();
        osname.setBounds(10, 250, 100, 20);
        osname.setText("OS: "+System.getProperty("os.name"));
        JLabel username = new JLabel();
        username.setBounds(170, 250, 100, 20);
        username.setText("User: "+System.getProperty("user.name"));
        downInfo = new JLabel();
        downInfo.setBounds(400, 250, 200, 20);


        JTextField urlText = new JTextField(30);
        urlText.setBounds(120, 15, 450, 30);

        JRadioButton all = new JRadioButton("All");
        all.setSelected(true);
        all.setBounds(400, 65, 50, 20);
        JRadioButton audio = new JRadioButton("Audio");
        audio.setBounds(460, 65, 70, 20);
        ButtonGroup group = new ButtonGroup();
        group.add(all);
        group.add(audio);

        JButton choseButton = new JButton("Save to");
        choseButton.setBounds(10, 110, 150,30);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        final String[] savePlace = {null};
        String system = osname.getText();
        String user = username.getText();
        String defaultPath = system.equals("Linux") ? "/home/"+user.split(" ")[1]+"/Рабочий стол" : "C:\\Users\\"+user.split(" ")[1]+"\\Desktop";
        System.out.println("Default path : "+ defaultPath);


        choseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выберите папку для сохранения");
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File res = fileChooser.getSelectedFile();
                    savePlace[0] = res.toString();
                    System.out.println("SavePalce : "+savePlace[0]);
//                    JOptionPane.showMessageDialog(frame, fileChooser.getSelectedFile());
                }
            }
        });

        JButton buttonDownload = new JButton("Download");
        buttonDownload.setBounds(10, 60, 200, 30);
        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoLabel.setText("");
                String filePath = savePlace[0]==null ? defaultPath : savePlace[0];
                System.out.println("File path : " +filePath);

                File outputDir = new File(filePath);
                if (!urlText.getText().equals("")) {
                    videoId = urlText.getText().substring(urlText.getText().indexOf("v=") + 2);
                } else {
                    infoLabel.setText("Введена неверная ссылка");
                }

                try {
                    Enumeration<AbstractButton> elements = group.getElements();
                    while (elements.hasMoreElements()) {
                        AbstractButton button = elements.nextElement();
                        if (button.isSelected() && button.getText().equals("All")) {
                            YoutubeVideo video = downloader.getVideo(videoId);
                            VideoDetails videoDetails = video.details();
                            String filename = videoDetails.title().replaceAll("//", " ");
                            List<AudioVideoFormat> formats = video.videoWithAudioFormats();

                            performDownload(formats.get(formats.size()-1), outputDir, filename,video);

                        } else if(button.isSelected() && button.getText().equals("Audio")){
                            YoutubeVideo video = downloader.getVideo(videoId);
                            VideoDetails videoDetails = video.details();
                            List<AudioFormat> formats = video.audioFormats();
                            String filename = videoDetails.title().replaceAll("//", " ");
                            performDownload(formats.get(formats.size()-1), outputDir, filename, video);

                        }

                    }
//                    Thread.currentThread().join();

                } catch (YoutubeException youtubeException) {
                    youtubeException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        });

        frame.add(all);
        frame.add(audio);
        frame.add(urlLabel);
        frame.add(urlText);
        frame.add(buttonDownload);
        frame.add(infoLabel);
        frame.add(osname);
        frame.add(username);
        frame.add(choseButton);
        frame.add(downInfo);
//        frame.add(fileChooser);
        frame.setVisible(true);

//        try {

//            List<AudioVideoFormat> audioVideoFormats = video.videoWithAudioFormats();
//            audioVideoFormats.forEach(e -> System.out.println("Audio uality " + e.audioQuality() + ", video quality " + e.videoQuality() + " - " + e.url()));


//            List<AudioFormat> audioFormats = video.audioFormats();
//            audioFormats.forEach(e -> System.out.println(e.averageBitrate() + " download link " + e.url()));

//            File outputDir = new File("videoAudio");
//            video.downloadAsync(audioVideoFormats.get(0), outputDir, new OnYoutubeDownloadListener() {
//                @Override
//                public void onDownloading(int i) {
//                    System.out.printf("\b\b\b\b\b%d%%", i);
//                }
//
//                @Override
//                public void onFinished(File file) {
//                    System.out.printf("Finish video: %s", file);
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    System.err.println("Error: "+throwable.getMessage());
//                }
//            });

//        } catch (YoutubeException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();

    }

    public void performDownload(Format format, File dir, String filename, YoutubeVideo video){
        try {
            video.downloadAsync(format, dir, filename, new OnYoutubeDownloadListener() {
                    @Override
                    public void onDownloading(int i) {
                        downInfo.setText(String.valueOf(i)+"%");
                    }

                    @Override
                    public void onFinished(File file) {
                        downInfo.setText("Downloaded successfull");
                        System.out.printf("Finish video: %s", file);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Error: "+throwable.getMessage());
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (YoutubeException e) {
            e.printStackTrace();
        }
    }
}
