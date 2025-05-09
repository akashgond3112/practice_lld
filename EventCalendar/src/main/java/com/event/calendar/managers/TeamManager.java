package com.event.calendar.managers;

import com.event.calendar.dao.TeamDao;
import com.event.calendar.dao.UserDao;
import com.event.calendar.models.Team;
import com.event.calendar.models.TimeSlot;
import com.event.calendar.models.User;

import java.util.List;

public class TeamManager {

    private TeamDao teamDao;
    private UserManager userManager;

    public TeamManager(UserManager userManager) {
        this.userManager = userManager;
        this.teamDao = new TeamDao();
    }

    public Team createTeam(String teamName, List<String> userNames) {
        validateUsers(userNames);
        Team team = new Team(teamName);
        for (String userName : userNames) {
            User user = userManager.getUser(userName);
            userManager.setTeam(user, team);
            team.addTeamMember(user);
        }
        teamDao.createTeam(team);
        return team;
    }

    public Team getTeam(String teamName) {
        return teamDao.getTeam(teamName);
    }

    private void validateUsers(List<String> userNames) {
        for(String userName : userNames) {
            boolean userAlreadyPartOfTeam = userManager.checkIfUserPartOfAnyTeam(userName);
            if (userAlreadyPartOfTeam) {
                throw new RuntimeException("User " + userName + " already part of another team");
            }
        }
    }



}
