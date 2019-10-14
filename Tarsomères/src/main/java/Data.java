import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@lombok.Data
@Builder
@JsonIgnoreProperties()
public class Data {
    private String product;
    private String T1_PTEC;
    private int T1_PAPP;
    private int T1_BASE;
    private String T2_PTEC;
    private int T2_PAPP;
    private int T2_BASE;
    private int INDEX_C1;
    private int INDEX_C2;
}