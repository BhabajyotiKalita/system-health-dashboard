package com.dashboard;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sun.management.OperatingSystemMXBean;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String dashboard(Model model) {

        try {
            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            // CPU Usage
            double cpuUsage = osBean.getCpuLoad() * 100;

            // RAM Usage
            long totalMemory = osBean.getTotalMemorySize();
            long freeMemory = osBean.getFreeMemorySize();
            long usedMemory = totalMemory - freeMemory;

            double memoryUsage = ((double) usedMemory / totalMemory) * 100;

            // Disk Usage
            File disk = new File(System.getProperty("user.dir"));

            long totalDisk = disk.getTotalSpace();
            long freeDisk = disk.getFreeSpace();
            long usedDisk = totalDisk - freeDisk;

            double diskUsage = ((double) usedDisk / totalDisk) * 100;

            // Hostname
            String hostname = InetAddress.getLocalHost().getHostName();

            // OS
            String operatingSystem = System.getProperty("os.name");

            // Java Version
            String javaVersion = System.getProperty("java.version");

            // Application Uptime
            long uptimeMilliseconds = ManagementFactory.getRuntimeMXBean().getUptime();

            long totalSeconds = uptimeMilliseconds / 1000;

            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            long seconds = totalSeconds % 60;

            String uptime = hours + "h " +
                    minutes + "m " +
                    seconds + "s";

            // Send values to HTML
            model.addAttribute(
                    "cpu",
                    String.format("%.1f", cpuUsage));

            model.addAttribute(
                    "memory",
                    String.format("%.1f", memoryUsage));

            model.addAttribute(
                    "disk",
                    String.format("%.1f", diskUsage));

            model.addAttribute("hostname", hostname);
            model.addAttribute("os", operatingSystem);
            model.addAttribute("javaVersion", javaVersion);
            model.addAttribute("uptime", uptime);

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    "Unable to retrieve system information.");
        }

        return "dashboard";
    }
}