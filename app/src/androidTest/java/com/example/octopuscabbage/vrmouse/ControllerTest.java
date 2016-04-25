package com.example.octopuscabbage.vrmouse;

import com.example.octopuscabbage.vrmouse.robot.RobotNetworkClient;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by octopuscabbage on 4/24/16.
 */
public class ControllerTest extends ApplicationTest{

    public void testControllerRecievesCommands(){
        RobotNetworkClient networkClient =  new MockRobotNetworkClient();
        networkClient.sendTurnDirection(10);
        networkClient.sendTurnDirection(10);
        networkClient.sendTurnDirection(10);
        Assert.assertEquals(3,((MockRobotNetworkClient)networkClient).commands.size());
    }

    private enum CommandEnum{
           TURN
    }
    private class Command{
        private CommandEnum command;
        private Object payload;

        public Command(CommandEnum command, Object payload){
            this.command = command;
            this.payload = payload;
        }
        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }

        public CommandEnum getCommand() {
            return command;
        }

        public void setCommand(CommandEnum command) {
            this.command = command;
        }
    }
    private class MockRobotNetworkClient implements RobotNetworkClient {
        List<Command> commands;
        public MockRobotNetworkClient(){
            commands = new ArrayList<>();
        }
        @Override
        public void sendTurnDirection(int angle) {
            commands.add(new Command(CommandEnum.TURN,angle));
        }

    }
}
