package honkytonky.objects;

import static honkytonky.misc.ANSI_Color_Codes.ANSI_GREEN;
import static honkytonky.misc.ANSI_Color_Codes.ANSI_RESET;

import honkytonky.misc.ExpTable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Player extends Actor {

    @XmlElement
    private int temporaryDefBoost;

    @XmlElement
    private int experience;

    @XmlElement
    private int gold;

    @XmlElement
    private Weapon weapon;

    @XmlElement
    private Armor armor;

    @XmlElement
    private Room currentRoom;

    @XmlElementWrapper(name="inventory")
    @XmlElement(name="item")
    private final List<Item> inventory;

    @XmlElementWrapper(name="potions")
    @XmlElement(name="potion")
    private final Map<Potion, Integer> playersPotions;

    @XmlTransient
    private final ExpTable expTable = new ExpTable();

    // Needs NoArgsConstructor for JAXB to work
    public Player() {
        super(null, 0,0,0);
        inventory = null;
        playersPotions = null;
    }

    public Player(@NonNull String name, int maxHP, @NonNull Weapon weapon, @NonNull Armor armor, @NonNull Room currentRoom) {

        super(name, maxHP, 1, weapon.getDamage());

        //@formatter:off
        this.currentRoom        = currentRoom;
        this.temporaryDefBoost  = 0;
        this.experience         = 0;
        this.gold               = 0;
        this.weapon             = weapon;
        this.armor              = armor;
        this.inventory          = new ArrayList<>();
        this.playersPotions     = new HashMap<>();
        //@formatter:on

        inventory.add(weapon);
        inventory.add(armor);
    }

    /**
     * give the player some additional armor points based on his current armor points
     */
    public void giveTemporaryDefBoost() {
        float defBoost = ((float) this.armor.getArmorPoints() / 2);

        if (defBoost > 10) {
            this.temporaryDefBoost = 2;
        } else if (defBoost < 1) {
            this.temporaryDefBoost = 1;
        } else {
            this.temporaryDefBoost = (int) defBoost;
        }
    }

    /**
     * reset the temporary armor points boost
     */
    public void resetTemporaryDefBoost() {
        this.temporaryDefBoost = 0;
    }

    /**
     * increase players experience by a set amount
     * @param amount amount to increase exp by
     */
    public void increaseExperience(int amount) {
        this.experience += amount;
    }

    /**
     * compare players current level und exp with exptable and may level him up
     */
    public void checkForLevelUp() {
        if (expTable.hasLevelUp(this.getLevel(), this.experience)) {
            this.setLevel(getLevel() + 1);

            System.out.println("\nYou have leveled up!\n");
            System.out.println("Your new Level is: " + ANSI_GREEN + this.getLevel() + ANSI_RESET);
        }
    }

    /**
     * increase players gold by a set amount
     * @param amount amount to increase gold by
     */
    public void giveGold(int amount) {
        this.gold += amount;
    }

    /**
     * raise players hp and remove the used potion from his inventory and potionlist
     * @param potion the used potion
     */
    public void usePotion(Potion potion) {
        healPlayer(potion.getAmount());
        inventory.remove(potion);
        playersPotions.put(potion, playersPotions.get(potion) - 1);
        System.out.println("You were healed by " + ANSI_GREEN + potion.getAmount() + ANSI_RESET + " health points!");
    }

    /**
     * increase players healthpoints - if hp is >maxHP set it to maxHP
     * @param amount amount to increase healthpoints by
     */
    private void healPlayer(int amount) {
        this.setHp(this.getHp() + amount);

        if (this.getHp() > this.getMaxHP()) {
            this.setHp(this.getMaxHP());
        }
    }

    /**
     * calculate how much percent the player has reached to the next level
     * @return percentage to level up
     */
    public float getPercentalExperience() {
        return expTable.calculatePercentalExperience(experience, getLevel());
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
