package com.draftoutmc.draftout.lockout;

import com.draftoutmc.draftout.Lockout;
import com.draftoutmc.draftout.lockout.goals.EmptyGoal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.reflect.ConstructorUtils;

public class GoalRegistry {
   public static final GoalRegistry INSTANCE = new GoalRegistry();
   private final Map<String, Class<? extends Goal>> registry = new LinkedHashMap();
   private final Map<String, GoalDataGenerator> goalDataGenerators = new HashMap();

   private GoalRegistry() {
   }

   public void register(String id, Class<? extends Goal> goalClass) {
      this.register(id, goalClass, (GoalDataGenerator)null);
   }

   public void register(String id, Class<? extends Goal> goalClass, GoalDataGenerator goalDataGenerator) {
      if (this.registry.containsKey(id)) {
         Lockout.log("Goal with id " + id + " has already been registered.");
      } else {
         this.registry.put(id, goalClass);
         this.goalDataGenerators.put(id, goalDataGenerator);
      }
   }

   public boolean isRegistered(String id) {
      return this.registry.containsKey(id);
   }

   public Goal newGoal(String id, String data) {
      try {
         return (Goal)ConstructorUtils.invokeConstructor((Class)this.registry.get(id), new Object[]{id, data == null ? "null" : data});
      } catch (Exception var4) {
         return new EmptyGoal(id, (String)null);
      }
   }

   public boolean isGoalValid(String id, String data) {
      try {
         ConstructorUtils.invokeConstructor((Class)this.registry.get(id), new Object[]{id, data});
         return true;
      } catch (Exception var4) {
         return false;
      }
   }

   public Optional<GoalDataGenerator> getDataGenerator(String id) {
      return this.goalDataGenerators.get(id) != null ? Optional.of((GoalDataGenerator)this.goalDataGenerators.get(id)) : Optional.empty();
   }

   public List<String> getRegisteredGoals() {
      return new ArrayList(this.registry.keySet());
   }

   public Class<? extends Goal> get(String id) {
      return (Class)this.registry.get(id);
   }
}
