/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.player;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author user
 */
public class FXMLDocumentController extends AnchorPane implements Initializable, ChangeListener<String> {

    @FXML
    private Label titleLabel, ArtistLabel, startTime, endTime;
    @FXML
    private ImageView playButtonImage, pauseButtonImage, nextButton, previousButton;
    @FXML
    private Slider slider, volumeSlider;
    @FXML
    private ProgressBar progressbar;
    @FXML
    private ImageView mp3image, imageSmallMp3, volumeButton, muteButton;

    @FXML
    private JFXListView<String> listFiles = new JFXListView<>();

    DecimalFormat df = new DecimalFormat("#.##");
    // variables
    boolean oncePlayed = false;
    MediaPlayer mediaPlayer;
    MediaView mediaView;

    public ObservableList observableList = FXCollections.observableArrayList();
    public List<String> listPath = new ArrayList();
    String path = null;
    @FXML
    private ProgressBar Volumeprogressbar;
    private Label track;
    @FXML
    private Label title;
    @FXML
    private Label Album;
    @FXML
    private Pane homePane;
    @FXML
    private Label homeText;

    @FXML
    private ImageView homeClickedImage;
    @FXML
    private ImageView homeUnclickedDark;
    private ImageView SideNavPanelImage;
    @FXML
    private Pane sideNavPanel;
    private ImageView navButton;
    @FXML
    private ImageView redFavourite;
    @FXML
    private ImageView whiteFavourite;
    @FXML
    private Label year;
    @FXML
    private Label Composer;
    @FXML
    private Label Publisher;
    @FXML
    private Label songLabel;
    @FXML
    private Label Artist;
    @FXML
    private JFXTextField searchTextField;
    @FXML
    private JFXListView<String> searchResultList = new JFXListView<>();
    public ObservableList SearchObservableList = FXCollections.observableArrayList();

    public FXMLDocumentController() {

    }

    public void hidesidepanel() {

        sideNavPanel.setVisible(false);
        SideNavPanelImage.setVisible(false);
        navButton.setVisible(true);

    }

    public void displaynav() {

        System.out.println("displayyyy");
        sideNavPanel.setVisible(true);
        SideNavPanelImage.setVisible(true);
        navButton.setVisible(false);

    }

    @FXML
    public void homeClicked() {

        homeUnclickedDark.setVisible(false);
        homeClickedImage.setVisible(true);
        homeText.setTextFill(Color.web("#2c87fd", 0.8));

    }

    @FXML
    public void play() throws IOException {
        if (oncePlayed == false) {

            //playing music first time;
            //Instantiating MediaView class   
            oncePlayed = true;

        } else {

            Duration currentTime = mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentTime);
        }

        updatesValues();
        mediaPlayer.play();

    }

    public void updatesValues() {
        playButtonImage.setVisible(false);
        pauseButtonImage.setVisible(true);

        String stopTime = df.format(mediaPlayer.getTotalDuration().toMinutes());
        endTime.setText(stopTime);
        System.out.println("changed Time value" + stopTime);

//            System.out.println(mediaPlayer.getTotalDuration().toSeconds());
//            slider.setMax(mediaPlayer.getStopTime().toSeconds());
//            slider.setMin(mediaPlayer.getStartTime().toSeconds());
        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {

                sliderUpdate();

            }
        });

        //
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((newValue.doubleValue() - oldValue.doubleValue() > 0.2) || ((newValue.doubleValue() < oldValue.doubleValue())) && oldValue.doubleValue() - newValue.doubleValue() > 0.2) {
                    progressbar.setProgress(newValue.doubleValue() / 100);
                    double musicChange = (newValue.doubleValue() * (mediaPlayer.getStopTime()).toSeconds()) / 100;
                    Duration duration = Duration.seconds(musicChange);
                    mediaPlayer.seek(duration);
                }

            }
        });

    }

    public void sliderUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Updating to the new time value 
                // This will move the slider while running your video 
                slider.setValue((mediaPlayer.getCurrentTime().toSeconds() * 100) / mediaPlayer.getStopTime().toSeconds());
                double musicProgress = (mediaPlayer.getCurrentTime().toSeconds()) / (mediaPlayer.getStopTime().toSeconds());

                progressbar.setProgress(musicProgress);
                DecimalFormat df = new DecimalFormat("#.##");
                String currentTime = df.format(mediaPlayer.getCurrentTime().toMinutes());
                System.out.println(currentTime);
                startTime.setText(currentTime);
                if (slider.getValue() == 100.0) {
                    System.out.println(" nexttt song" + slider.getValue());
                    next();

                }
            }
        });
    }

    @FXML
    public void pause() {
        if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {

            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
        } else {

            mediaPlayer.pause();
            pauseButtonImage.setVisible(false);

            playButtonImage.setVisible(true);
        }
    }
    int randomfile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        volumeSlider.setValue(100);
        Volumeprogressbar.setProgress(1);
        extract("F:\\music");
        Random randam = new Random();
        randomfile = randam.nextInt(listPath.size());
        path = listPath.get(randomfile);
        Media media = new Media(new File(path).toURI().toString());
        //Instantiating MediaPlayer class   
        mediaPlayer = new MediaPlayer(media);
        //Instantiating MediaView class   
        mediaView = new MediaView(mediaPlayer);
        mediaPlayer.setVolume(1);
        getChildren().add(mediaView);
        defaultLabels();
        fileDetails();
        listitemSelected();
        initialVolume();
        songLabel.setFont(Font.loadFont("file:resources/font/Muli-Regular.ttf", 120));
        search();

    }
    Double volume = 1.0;

    public void initialVolume() {
        System.out.println(mediaPlayer);

        String currentTime = df.format(mediaPlayer.getCurrentTime().toMinutes());
        System.out.println(currentTime);
        startTime.setText(currentTime);

        System.out.println(mediaPlayer.getTotalDuration().toMinutes());
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if ((oldValue.doubleValue() != newValue.doubleValue()) && (newValue.doubleValue() != 0.0)) {
                    muteButton.setVisible(false);
                    volumeButton.setVisible(true);
                    volume = newValue.doubleValue() / 100.0;
                    System.out.println(oldValue.doubleValue());

                    if (mediaPlayer != null) {

                        mediaPlayer.setVolume(volume);
                        Volumeprogressbar.setProgress(volume);

                    }
                    if (newValue.doubleValue() == 0.0) {

                        doMute();
                    }

                }

            }

        });
    }

    public void fileDetails() {

        //start of mp3 metadata extract
        try {
            Mp3File mp3file = new Mp3File(path);
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                System.out.println("Track: " + id3v2Tag.getTrack());
                System.out.println("Artist: " + id3v2Tag.getArtist());
                System.out.println("Title: " + id3v2Tag.getTitle());
                System.out.println("Album: " + id3v2Tag.getAlbum());
                System.out.println("Year: " + id3v2Tag.getYear());
                System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
                System.out.println("Comment: " + id3v2Tag.getComment());
                System.out.println("Lyrics: " + id3v2Tag.getLyrics());
                System.out.println("Composer: " + id3v2Tag.getComposer());
                System.out.println("Publisher: " + id3v2Tag.getPublisher());
                System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
                System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
                System.out.println("Copyright: " + id3v2Tag.getCopyright());
                System.out.println("URL: " + id3v2Tag.getUrl());
                System.out.println("Encoder: " + id3v2Tag.getEncoder());
                byte[] albumImageData = id3v2Tag.getAlbumImage();
                if (albumImageData != null) {
                    System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
                    System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
                }

                if (id3v2Tag.getTitle() != null) {
                    titleLabel.setText(id3v2Tag.getTitle());
                    title.setText(id3v2Tag.getTitle());
                }

                if (id3v2Tag.getAlbum() != null) {
                    Album.setText(id3v2Tag.getAlbum());
                }

                if (id3v2Tag.getArtist() != null) {
                    ArtistLabel.setText(id3v2Tag.getArtist());
                    Artist.setText(id3v2Tag.getArtist());
                }

                if (id3v2Tag.getYear() != null) {
                    year.setText(id3v2Tag.getYear());
                }
                if (id3v2Tag.getComposer() != null) {
                    Composer.setText(id3v2Tag.getComposer());
                }
                if (id3v2Tag.getPublisher() != null) {
                    Publisher.setText(id3v2Tag.getPublisher());
                }

                if (albumImageData != null) {

                    RandomAccessFile file = new RandomAccessFile("album-artwork.jpg", "rw");
                    file.write(albumImageData);
                    file.close();
                    FileInputStream fis = new FileInputStream("album-artwork.jpg");
                    Image image = new Image(fis);
                    mp3image.setImage(image);
                    imageSmallMp3.setImage(image);
                } else {

                    FileInputStream fis = new FileInputStream("E:\\projects\\MusicPlayer\\src\\musicplayer\\images\\albumartplaceholder.jpg");
                    Image image = new Image(fis);
                    mp3image.setImage(image);
                    imageSmallMp3.setImage(image);
                }

            } else if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v2Tag = mp3file.getId3v1Tag();
                System.out.println("Track: " + id3v2Tag.getTrack());
                System.out.println("Artist: " + id3v2Tag.getArtist());
                System.out.println("Title: " + id3v2Tag.getTitle());
                System.out.println("Album: " + id3v2Tag.getAlbum());
                System.out.println("Year: " + id3v2Tag.getYear());
                System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
                System.out.println("Comment: " + id3v2Tag.getComment());

                if (id3v2Tag.getTitle() != null) {
                    titleLabel.setText(id3v2Tag.getTitle());
                    title.setText(id3v2Tag.getTitle());
                }

                if (id3v2Tag.getAlbum() != null) {
                    Album.setText(id3v2Tag.getAlbum());
                }
                if (id3v2Tag.getArtist() != null) {
                    ArtistLabel.setText(id3v2Tag.getArtist());
                    Artist.setText(id3v2Tag.getArtist());
                }
                if (id3v2Tag.getYear() != null) {
                    year.setText(id3v2Tag.getYear());
                }

                FileInputStream fis = new FileInputStream("E:\\projects\\MusicPlayer\\src\\musicplayer\\images\\albumartplaceholder.jpg");
                Image image = new Image(fis);
                mp3image.setImage(image);
                imageSmallMp3.setImage(image);
            }
        } catch (InvalidDataException | UnsupportedTagException | IOException e) {
            e.printStackTrace();

        }
    }

// file extract from folder
    public void extract(String p) {
        File f = new File(p);
        File l[] = f.listFiles();
        for (File x : l) {
            if (x == null) {
                return;
            }
            if (x.isHidden() || !x.canRead()) {
                continue;
            }
            if (x.isDirectory()) {
                extract(x.getPath());
            } else if (x.getName().endsWith(".mp3")) {
//            { System.out.println(x.getPath()+"\\"+x.getName());

                observableList.add(x.getName());
                listPath.add(x.getPath());

            }

        }

        listFiles.getItems().addAll(observableList);

    }

    public void listitemSelected() {

        listFiles.getSelectionModel().selectedItemProperty().addListener(this);

    }
    int index;

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        try {
            if (oldValue == null) {
                oldValue = "1";
            }
            if (!oldValue.equals(newValue)) {

                index = listFiles.getSelectionModel().getSelectedIndex();
                playfile(index);

            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    //previous button fuctionality  
    @FXML
    public void previous() {

        int i = listFiles.getSelectionModel().getSelectedIndex();
        if (index < 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Music Player Message");
            alert.setHeaderText(null);
            alert.setContentText("Reached fisrt Song");
            alert.show();
        } else {
            index--;

            playfile(index);
        }

    }

    //next button functionality
    @FXML
    public void next() {
        int i = listFiles.getSelectionModel().getSelectedIndex();
        if (index > listPath.size()) {
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Music Player Message");
                alert.setHeaderText(null);
                alert.setContentText("Reached last Song");
                alert.show();
            }
        } else {
            index++;
            playfile(index);
        }
    }

    @FXML
    public void doMute() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0.0);
        }
        volumeButton.setVisible(false);
        muteButton.setVisible(true);

    }

    @FXML
    public void doUnmute() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(1.0);
            volumeSlider.setValue(volume * 100);
            Volumeprogressbar.setProgress(volume);
        }
        muteButton.setVisible(false);
        volumeButton.setVisible(true);
    }

    // play file from list get path
    public void playfile(int i) {

        String itemPath = listPath.get(i);

        path = itemPath;
        defaultLabels();
        fileDetails();
        double songvolume = 100;
        if (mediaPlayer != null) {
            songvolume = mediaPlayer.getVolume();
            mediaPlayer.dispose();
        }

        Media media = new Media(new File(path).toURI().toString());

        //Instantiating MediaPlayer class   
        mediaPlayer = new MediaPlayer(media);
        //Instantiating MediaView class   
        mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setVolume(songvolume);

        mediaPlayer.play();

        getChildren().add(mediaView);
        updatesValues();
        String stopTime = df.format(mediaPlayer.getTotalDuration().toMinutes());
        endTime.setText(stopTime);
        System.out.println(stopTime);
        String stopTime1 = df.format(mediaPlayer.getStopTime().toMinutes());
        endTime.setText(stopTime1);
        System.out.println(stopTime1);
        mediaPlayer.setVolume(volume);

    }

    //defaultLabels():
    public void defaultLabels() {
        String unkonwn = "UNKNOWN";

        title.setText(unkonwn);
        titleLabel.setText(unkonwn);
        ArtistLabel.setText(unkonwn);
        Artist.setText(unkonwn);
        year.setText(unkonwn);
        Composer.setText(unkonwn);
        Publisher.setText(unkonwn);

    }

    @FXML
    private void makeUnfavourite(MouseEvent event) {
        whiteFavourite.setVisible(true);
        redFavourite.setVisible(false);
    }

    @FXML
    private void makeFavourite(MouseEvent event) {
        redFavourite.setVisible(true);
        whiteFavourite.setVisible(false);

    }
    // store search result in hashmap
    HashMap<String, Integer> searchMap = new HashMap<>();

    @FXML
    private void search() {

        searchTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            System.out.println("Value changed from " + oldValue + "to " + newValue);
            searchMap.clear();
            searchResultList.getItems().removeAll(SearchObservableList);
//                 searchResultList.getItems().clear();

            SearchObservableList.clear();
//                Iterator iterator= searchResultList.getItems().iterator();
//               while(iterator.hasNext())
//               {
//                   String str= iterator.next().toString();
//                   System.out.println(" ITEMS FROM JFX LIST"+str);
//                   
//               }

            if (oldValue.equals(null)) {
                oldValue = "";
            }
            if (oldValue != newValue) {

                searchResultList.setVisible(false);
                System.out.println(listPath.contains(newValue));
                Iterator i = listPath.iterator();
                while (i.hasNext() && !newValue.equals("")) {
                    String result = i.next().toString();
                    //    String realResult=i.next().toString();

                    if (result.contains(newValue)) {
                        searchResultList.setVisible(true);
                        System.out.println("Found !!!!!" + result);
                        //  String labelText= result.substring(9);
                        SearchObservableList.add(result);
                        int key = (listPath.indexOf(result));
                        searchMap.put(result, key);
                    }

                }
                searchResultList.getItems().addAll(SearchObservableList);
            }
        });

        searchResultList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

            try {
                if (oldValue == null) {
                    oldValue = "1";
                }
                if (!oldValue.equals(newValue)) {

                    String fileString = searchResultList.getSelectionModel().getSelectedItem();
                    index = searchMap.get(fileString);

                    playfile(index);

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        });
    }

}
