
package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.util.Optional;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.ScrollPane;
import javafx.collections.*;
import javafx.geometry.Pos;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents my ApiApp that implements mutiple API calls to display location and weather
 * information.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;

    private HBox topLayer;
    private Label locationLabel;
    private Button getLocation;

    private HBox infoLayer;
    private VBox leftInfo;
    private VBox rightInfo;

    private ImageView staticMap;
    private ImageView heatMap;
    private ImageView weatherVisual;

    private Text weatherInfo;
    private TextFlow weatherInfoContainer;
    private ScrollPane scrollPane;

    private Label searchLabel;
    private TextField searchText;

    private HBox progressBarLayer;
    private ProgressBar progressBar;

    private ComboBox<String> ipBox;
    private String[] types = {
        "My Location",  "custom", "76.215.135.100",
        "76.215.123.170", "76.115.123.170", "76.255.135.108"
    };

    private boolean allowed = false;

    private IPApiResponse ipResponse = new IPApiResponse();
    private WeatherVisualResponse wvr = new WeatherVisualResponse();
    private GoogleMapsAirQualityResponse gmaqr = new GoogleMapsAirQualityResponse();
    private GoogleMapsApiResponse gmar = new GoogleMapsApiResponse();
    private NWSResponse nws = new NWSResponse();

    /**
     * Method that creates the pop up.
     *
     * @param cause the cause of the error
     */
    private void errorPopUp(Throwable cause) {
        Platform.runLater(() -> {
            this.getLocation.setDisable(false);
            this.progressBar.setProgress(1.0);
            TextArea text = new TextArea(cause.toString());
            text.setEditable(false);

            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setContent(text);
            alert.setResizable(true);

            alert.showAndWait();
        });
    }


    /**
     * Makes a call to the Weather Visual API and creates the results.
     *
     * @param latitude the latitude of the call
     * @param longitude the longitude of the call
     */
    public void displayWeatherMap(String latitude, String longitude) {

        this.wvr = new WeatherVisualResponse();
        this.wvr.createURI(latitude, longitude);
        this.wvr.createResults("weatherMap.png");
    }

    /**
     * Makes a call to the Google Air Quality Api and creates the results.
     *
     * @param latitude the latitude of the call
     * @param longitude the longitude of the call
     */
    public void displayAirQualityMap(String latitude, String longitude) {

        this.gmaqr = new GoogleMapsAirQualityResponse();
        this.gmaqr.createURI(latitude, longitude);
        this.gmaqr.createResults("heatmap.png");

    }

    /**
     * Makes a call to the Google Static map Api and creates the results.
     *
     * @param city the city of the call
     * @param country the country of the call
     */
    public void displayStaticMap(String city, String country) {

        this.gmar = new GoogleMapsApiResponse();
        this.gmar.createURI(city, country, "600x400");
        this.gmar.createResults("staticmap.png");
    }

    /**
     * Makes a call to the NWS API and creates and returns the results.
     *
     * @param latitude the latitude of the call
     * @param longitude the longitude of the call
     * @return the results of the API call
     */
    public String getWeatherInformation(String latitude, String longitude) {

        this.nws = new NWSResponse();
        this.nws.createURI(latitude, longitude);
        this.nws.createResults();
        return this.nws.returnResults();

    }

    /**
     * Takes the users IP or passed through IP and then makes subsuqent calls
     * to different methods to get responses and project images on the aplication window.
     *
     * @param IP ip or type of IP the method should get
     */
    public void useIPData(String IP) {
        try {
            Platform.runLater(() -> {
                this.progressBar.setProgress(0.0);
            });
            String ipAddress;
            if (IP == "user") {
                InetAddress localHost = InetAddress.getLocalHost();
                ipAddress = localHost.getHostAddress();
            } else {
                ipAddress = IP;
            };
            this.ipResponse = new IPApiResponse();
            this.ipResponse.createURI(ipAddress);
            this.ipResponse.createResults();
            Platform.runLater(() -> {
                this.progressBar.setProgress(1.0 / 9.0);
            });
            Object[] results = this.ipResponse.returnResults();
            String city = (String) results[0];
            String region = (String) results[1];
            String countryName = (String) results[2];
            String latitude = (String) results[3];
            String longitude = (String) results[4];
            this.displayWeatherMap(latitude, longitude);
            Platform.runLater(() -> {
                this.progressBar.setProgress(2.0 / 9.0);
            });
            this.displayAirQualityMap(latitude, longitude);
            Platform.runLater(() -> {
                this.progressBar.setProgress(3.0 / 9.0);
            });
            this.displayStaticMap(city, countryName);
            Platform.runLater(() -> {
                this.progressBar.setProgress(4.0 / 9.0);
            });
            String weatherI = this.getWeatherInformation(latitude, longitude);
            Platform.runLater(() -> {
                this.progressBar.setProgress(5.0 / 9.0);
            });
            Platform.runLater(() -> {
                this.locationLabel.setText("Location: " + city + ", " + region + ", "
                                           + countryName);
                this.progressBar.setProgress(6.0 / 9.0);
                this.weatherVisual.setImage(new Image("file:weatherMap.png"));
                this.heatMap.setImage(new Image("file:heatmap.png"));
                this.progressBar.setProgress(8.0 / 9.0);
                this.staticMap.setImage(new Image("file:staticmap.png"));
                this.weatherInfo.setText(weatherI);
                this.progressBar.setProgress(1.0);
                this.getLocation.setDisable(false);
            });
        } catch (Exception e) {
            errorPopUp(e);
        }
    }

    /**
     * Makes thread and calls the useIPData method.
     *
     * @param Check the string and identifier passed through
     */
    public void makeThread(String check) {
        this.getLocation.setDisable(true);
        Thread t = new Thread(() -> useIPData(check));
        t.setDaemon(true);
        t.start();
    }

    /**
     * Checks the currentSelection of ipBox and then creates subsuquent
     * calls or feedbacks to the user.
     */
    public void locationVerification() {

        String currentSelection = this.ipBox.getValue();

        try {
            if (currentSelection  == "My Location") {
                if (!allowed) {
                    TextArea text = new TextArea("Clicking ok will allow the APP to use your IP "
                                                 + "Adress the Adress is not stored only to be used"
                                                 + " to find the users general location and is NOT "
                                                 + "needed to use the app pre set ips as well as "
                                                 + "the flexibility to enter own ip's is avaliable "
                                                 + "to use click cancel to not allow this feature "
                                                 + "and ok to allow this feature.");
                    text.setEditable(false);

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.getDialogPane().setContent(text);
                    alert.setResizable(true);

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent()) {
                        if (result.get() == ButtonType.OK) {
                            this.allowed = true;
                            makeThread("user");
                        }
                    }
                } else {
                    makeThread("user");
                }
            } else if (currentSelection == "custom") {
                String Text = this.searchText.getText().trim();
                if (Text.isEmpty()) {
                    throw new IllegalArgumentException("Text field in search box cannot be empty");
                }
                makeThread(Text);
            } else {
                makeThread(currentSelection);
            }
        } catch (IllegalArgumentException e) {
            errorPopUp(e);
        }
    }


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();

        this.topLayer = new HBox(10);
        this.topLayer.setAlignment(Pos.CENTER);

        this.locationLabel = new Label("Location: Unknown");
        this.getLocation = new Button("Get Location");

        this.getLocation.setOnAction(event -> {
            this.locationVerification();
        });

        this.ipBox = new ComboBox<String>(FXCollections.observableArrayList(types));
        this.ipBox.getSelectionModel().select("My Location");

        this.searchLabel = new Label("Input:");

        this.searchText = new TextField("Defualt");

        this.infoLayer = new HBox();

        this.leftInfo = new VBox();
        this.rightInfo = new VBox();
        this.progressBarLayer = new HBox();

        this.staticMap = new ImageView();
        this.staticMap.setFitWidth(350);
        this.staticMap.setFitHeight(250);
        this.staticMap.setPreserveRatio(false);
        this.staticMap.setImage(new Image("file:resources/readme-banner.png"));

        this.heatMap = new ImageView();
        this.heatMap.setFitWidth(350);
        this.heatMap.setFitHeight(250);
        this.heatMap.setPreserveRatio(false);
        this.heatMap.setImage(new Image("file:resources/readme-banner.png"));

        this.weatherVisual = new ImageView();
        this.weatherVisual.setFitWidth(350);
        this.weatherVisual.setFitHeight(250);
        this.weatherVisual.setPreserveRatio(false);
        this.weatherVisual.setImage(new Image("file:resources/readme-banner.png"));

        this.weatherInfo = new Text();
        this.weatherInfo.wrappingWidthProperty().set(400);

        this.weatherInfoContainer = new TextFlow(weatherInfo);

        this.scrollPane = new ScrollPane(weatherInfoContainer);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setPrefHeight(250);
        this.scrollPane.setPrefWidth(350);

        this.progressBar = new ProgressBar();
        this.progressBar.setProgress(0.0);
        this.progressBar.setPrefWidth(700);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {

        this.root.getChildren().addAll(topLayer,infoLayer,progressBarLayer);
        this.topLayer.getChildren().addAll(locationLabel, searchLabel,
                                           searchText, ipBox, getLocation);
        this.infoLayer.getChildren().addAll(leftInfo, rightInfo);
        this.leftInfo.getChildren().addAll(staticMap, heatMap);
        this.rightInfo.getChildren().addAll(weatherVisual, scrollPane);
        this.progressBarLayer.getChildren().addAll(progressBar);
    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // demonstrate how to load local asset using "file:resources/"
        // Image bannerImage = new Image("file:resources/readme-banner.png");
        // ImageView banner = new ImageView(bannerImage);
        // banner.setPreserveRatio(true);
        // banner.setFitWidth(640);

        // some labels to display information
        // Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        // root.getChildren().addAll(notice);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
