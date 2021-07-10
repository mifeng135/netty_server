package core.annotation.proto;


import lombok.Getter;
import lombok.Setter;

@Deprecated
@Getter
@Setter
public class ProtoInfo {
    private Class cls;
    private String[] importFile;
    private String protoFile;

    public void setImportFile(String[] importFile) {
        if (importFile.length > 0) {
            if (importFile[0].length() <= 0) {
                return;
            }
        }
        this.importFile = importFile;
    }
}
