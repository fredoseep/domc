package com.draftoutmc.draftout.lockout;

import com.draftoutmc.draftout.lockout.goals.util.GoalDataConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.world.item.DyeColor;

public class GoalDataGenerator {
   public static final List<DyeColor> ALL_DYES;
   private final List<GeneratorEntry<?, ?>> generators = new ArrayList();

   private GoalDataGenerator() {
   }

   public static GoalDataGenerator builder() {
      return new GoalDataGenerator();
   }

   public GoalDataGenerator withDye(DyeGenerator dyeGenerator) {
      return this.withDye(ALL_DYES, dyeGenerator);
   }

   public GoalDataGenerator withDye(List<DyeColor> dyeDomain, DyeGenerator dyeGenerator) {
      this.generators.add(new GeneratorEntry<>(dyeGenerator, List.copyOf(dyeDomain), GoalDataConstants::getDyeColorDataString));
      return this;
   }

   public GoalDataGenerator withLeatherArmorPiece(LeatherArmorPieceGenerator leatherArmorPieceGenerator) {
      this.generators.add(new GeneratorEntry<>(leatherArmorPieceGenerator, List.copyOf(GoalDataConstants.DATA_LEATHER_ARMOR), Function.identity()));
      return this;
   }

   public String generateData(List<DyeColor> attainableDyes) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < this.generators.size(); ++i) {
         if (i > 0) {
            sb.append("&");
         }

         GeneratorEntry<?, ?> entry = this.generators.get(i);
         Generator<?> generator = entry.generator();
         if (generator instanceof DyeGenerator dyeGenerator) {
            sb.append(dyeGenerator.generateData(new ArrayList<>(filterDyeDomain(attainableDyes, entry))));
         } else if (generator instanceof LeatherArmorPieceGenerator leatherArmorPieceGenerator) {
            sb.append(leatherArmorPieceGenerator.generateData(new ArrayList<>(GoalDataConstants.DATA_LEATHER_ARMOR)));
         }
      }

      return sb.toString();
   }

   public List<String> enumerateData() {
      List<String> data = List.of("");

      for(GeneratorEntry<?, ?> generator : this.generators) {
         data = data.stream().flatMap((prefix) -> generator.enumerateData().stream().map((value) -> prefix.isEmpty() ? value : prefix + "&" + value)).toList();
      }

      return data;
   }

   public List<Generator<?>> getGenerators() {
      return (List<Generator<?>>) (Object) this.generators.stream().map(GeneratorEntry::generator).toList();
   }

   public static List<DyeColor> allDyesExcept(DyeColor... excludedDyes) {
      List<DyeColor> excluded = List.of(excludedDyes);
      return ALL_DYES.stream().filter((dye) -> !excluded.contains(dye)).toList();
   }

   private static List<DyeColor> filterDyeDomain(List<DyeColor> attainableDyes, GeneratorEntry<?, ?> entry) {
      List<DyeColor> dyeDomain = (List<DyeColor>) entry.domain();
      Stream var10000 = attainableDyes.stream();
      Objects.requireNonNull(dyeDomain);
      return var10000.filter(dyeDomain::contains).toList();
   }

   static {
      ALL_DYES = List.of(DyeColor.BLACK, DyeColor.WHITE, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.BLUE, DyeColor.LIGHT_BLUE, DyeColor.ORANGE, DyeColor.RED, DyeColor.YELLOW, DyeColor.MAGENTA, DyeColor.PINK, DyeColor.PURPLE, DyeColor.GREEN, DyeColor.LIME, DyeColor.CYAN, DyeColor.BROWN);
   }

   private static record GeneratorEntry<D, V>(Generator<D> generator, List<V> domain, Function<V, String> serializer) {
      List<String> enumerateData() {
         return this.domain.stream().map(this.serializer).toList();
      }
   }

   @FunctionalInterface
   public interface Generator<D> {
      default String getGeneratorName() {
         throw new UnsupportedOperationException("Generator#getGeneratorName was not implemented.");
      }

      default boolean verify(String s) {
         throw new UnsupportedOperationException("Generator#verify was not implemented.");
      }

      String generateData(D var1);
   }

   @FunctionalInterface
   public interface DyeGenerator extends Generator<List<DyeColor>> {
      default String getGeneratorName() {
         return "Dye Color";
      }

      default boolean verify(String s) {
         return GoalDataConstants.getDyeColor(s) != null;
      }
   }

   @FunctionalInterface
   public interface LeatherArmorPieceGenerator extends Generator<List<String>> {
      default String getGeneratorName() {
         return "Leather Armor Piece";
      }

      default boolean verify(String s) {
         return GoalDataConstants.DATA_LEATHER_ARMOR.contains(s);
      }
   }
}
