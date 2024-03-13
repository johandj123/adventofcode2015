import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day23 {
    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = InputUtil.readAsLines("input23.txt").stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
        first(instructions);
        second(instructions);
    }

    private static void first(List<Instruction> instructions) {
        Runner runner = new Runner(instructions);
        runner.run();
        System.out.println(runner.registers.get("b"));
    }

    private static void second(List<Instruction> instructions) {
        Runner runner = new Runner(instructions);
        runner.registers.put("a", 1);
        runner.run();
        System.out.println(runner.registers.get("b"));
    }

    static class Runner {
        final List<Instruction> instructions;
        final Map<String,Integer> registers = new HashMap<>(){{
            put("a", 0);
            put("b", 0);
        }};
        int pc = 0;

        public Runner(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public void run() {
            while (pc < instructions.size()) {
                Instruction instruction = instructions.get(pc);
                if ("inc".equals(instruction.op)) {
                    registers.put(instruction.reg, registers.get(instruction.reg) + 1);
                    pc++;
                } else if ("hlf".equals(instruction.op)) {
                    registers.put(instruction.reg, registers.get(instruction.reg) / 2);
                    pc++;
                } else if ("tpl".equals(instruction.op)) {
                    registers.put(instruction.reg, registers.get(instruction.reg) * 3);
                    pc++;
                } else if ("jmp".equals(instruction.op)) {
                    pc += instruction.offset;
                } else if ("jio".equals(instruction.op)) {
                    if (registers.get(instruction.reg) == 1) {
                        pc += instruction.offset;
                    } else {
                        pc++;
                    }
                } else if ("jie".equals(instruction.op)) {
                    if ((registers.get(instruction.reg) % 2) == 0) {
                        pc += instruction.offset;
                    } else {
                        pc++;
                    }
                }
            }
        }
    }

    static class Instruction {
        String op;
        String reg;
        int offset;

        public Instruction(String s) {
            String[] sp = s.split(" |, ");
            op = sp[0];
            if ("jmp".equals(op)) {
                offset = Integer.parseInt(sp[1]);
            } else if (op.startsWith("ji")) {
                reg = sp[1];
                offset = Integer.parseInt(sp[2]);
            } else {
                reg = sp[1];
            }
        }
    }
}
