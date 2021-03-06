package swingy.model.character;

import swingy.utils.algorithms.random.Randomizers;

public class Artifact {
    private int id;
    private final ArtifactType artifactType;
    private final int bonus;
    private final boolean isEmpty;

    public Artifact(int itemLevel, boolean isEmpty, ArtifactType artifactType) {
        this.artifactType = artifactType;
        this.isEmpty = isEmpty;
        if (!isEmpty) {
            boolean isSuperItem = Randomizers.isSuperItem();
            bonus = Randomizers.getArtifactCharacteristic(itemLevel, isSuperItem);
        } else {
            bonus = 0;
        }
    }

    public Artifact(ArtifactType artifactType, int bonus, boolean isEmpty) {
        this.artifactType = artifactType;
        this.bonus = bonus;
        this.isEmpty = isEmpty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public ArtifactType getArtifactType() {
        return artifactType;
    }

    public int getBonus() {
        return bonus;
    }

    @Override
    public String toString() {
        String retVal = "Artifact type: " + artifactType ;
        if (artifactType == ArtifactType.ARMOR) {
            return retVal + ", defence bonus - " + bonus;
        } else if (artifactType == ArtifactType.HELM) {
            return retVal + ", hp bonus - " + bonus;
        } else {
            return retVal + ", attack bonus - " + bonus;
        }
    }
}
