package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init() {
    mockPrimary = mock(TorpedoStore.class);
    mockSecondary = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimary, mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Alternating() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, firstResult);
    assertEquals(true, secondResult);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_One_Empty() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, firstResult);
    assertEquals(true, secondResult);
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_First_Fires() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_None_Fires() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, firstResult);
    assertEquals(false, secondResult);
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_All_Single_Fires() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.ALL);
    boolean thirdResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, firstResult);
    assertEquals(true, secondResult);
    assertEquals(true, thirdResult);

    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Alternating_Second_Reloaded() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockSecondary.isEmpty()).thenReturn(false);

    boolean thirdResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, firstResult);
    assertEquals(true, secondResult);
    assertEquals(true, thirdResult);

    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Fire_First_Two_Times() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenReturn(true);

    boolean firstResult = ship.fireTorpedo(FiringMode.SINGLE);
    when(mockPrimary.isEmpty()).thenReturn(true);
    boolean secondResult = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, firstResult);
    assertEquals(false, secondResult);

    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_Fire_Second_Two_Times() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);

    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Empty() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, result);

    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Empty() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, result);

    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Secondary() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, result);

    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Secondary_Cant_Fire() {
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, result);

    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }
  @Test
  public void fireTorpedo_All_Secondary_Malfunction() {
    when(mockPrimary.isEmpty()).thenReturn(false);
    when(mockSecondary.isEmpty()).thenReturn(false);
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, result);

    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }
}
